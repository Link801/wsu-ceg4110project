'''
Author:Randy Forte
'''
from flask import Flask, send_file, request, Response
from werkzeug.utils import secure_filename
import argparse
import numpy as np
import tensorflow as tf
from PIL import Image
import sys
import os.path

###### Initialization code - we only need to run this once and keep in memory.
sess = tf.Session()
saver = tf.train.import_meta_graph('/var/www/seefood/saved_model/model_epoch5.ckpt.meta')
saver.restore(sess, tf.train.latest_checkpoint('/var/www/seefood/saved_model/'))
graph = tf.get_default_graph()
x_input = graph.get_tensor_by_name('Input_xn/Placeholder:0')
keep_prob = graph.get_tensor_by_name('Placeholder:0')
class_scores = graph.get_tensor_by_name("fc8/fc8:0")
######

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def upload_file():
   if request.method == 'POST':
       f = request.files['file']
       directory = os.path.join('/var/www/seefood/' + f.filename)
       f.save(directory)
       score = ai_call(directory)
       #save file in location based on score
       if np.argmax(score) == 1:
           #return "No food here... :disappointed: "
           dbDirectory = os.path.join('/var/www/seefood/notfoodimages/' + f.filename)
           f.save(dbDirectory)
       else:
           #return "Oh yes... I see food! :D"
           dbDirectory = os.path.join('/var/www/seefood/foodimages/' + f.filename)
           f.save(dbDirectory)
#        os.remove(directory)
       daterbase = open("/var/www/seefood/database.txt","a")
       daterbase.write(f.filename + "," + str(score) + "\n")
       daterbase.close()
       return str(score)

   return "invalid post request"

@app.route('/download/<file_name>')
def get_file(file_name):
   file = "/var/www/seefood/foodimages/" + file_name
   if(os.path.isfile(file)):
       return send_file(file)
   else:
       file = "/var/www/seefood/notfoodimages/" + file_name
       return send_file(file)

@app.route('/log')
def get_log():
   temp = open("/var/www/seefood/database.txt","r+")
   return Response(temp.read(),mimetype='text/plain')

def ai_call(system_arg):
   image_path = system_arg
   image = Image.open(image_path).convert('RGB')
   image = image.resize((227, 227), Image.BILINEAR)
   img_tensor = [np.asarray(image, dtype=np.float32)]
   scores = sess.run(class_scores, {x_input: img_tensor, keep_prob: 1.})
   return scores

if __name__ == '__main__':
   app.run(debug=False)
