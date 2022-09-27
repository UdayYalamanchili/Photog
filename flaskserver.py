import os 
from flask import Flask, request
import random
import time

ALLOWED_EXTENSIONS = ['jpg', 'jpeg']
app = Flask(__name__)

app.secret_key = 'the random string'

@app.route('/', methods=['POST'])
def upload_file():
    image = request.files['image']
    category = request.form['category']
    path="./"+category+"/"
    pathExists = os.path.exists(path)
    timestr = time.strftime("%Y%m%d-%H%M%S")
    name = timestr + ".jpg"
    if not pathExists:
        os.makedirs(path)
    
    image.save(path+name)
    print("Image Uploaded to the " + category + " folder!")
    return "Image Uploaded! to the " + category + " folder"

app.run(host="0.0.0.0", port=8000, debug=True)