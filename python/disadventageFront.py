from flask import Flask, request, render_template, url_for
from werkzeug.utils import secure_filename
from PIL import Image
import numpy as np
import os
import json
import onnxruntime

model_path = r'.\model\model.onnx'
img_cache_path = r'.\\img_cache\\'

app = Flask(__name__)

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_file():
    print(request)
    if 'filename' not in request.files:
        return '没有文件部分在请求中'

    # 获取文件名
    files = request.files.getlist('filename')
    if not files or files[0].filename == '':
        return '没有选择文件'

    if not os.path.exists(model_path): #检查模型文件是否存在
        return '模型不存在'
    # 加载模型
    sess = onnxruntime.InferenceSession(model_path)

    if not os.path.exists(img_cache_path):
        os.mkdir(img_cache_path)

    img_urls = []
    input = []
    for file in files:
        if not (file or file.filename.endswith(('.png', '.jpg', '.tif', '.bpm'))):
            continue

        # 获取文件期望保存位置
        filename = secure_filename(file.filename)
        cache_url = '.\\img_cache\\input_' + filename
        img_url = url_for('static', filename='output_' + filename)
        img_urls.append(img_url)

        # 处理图片为模型可接受的输入
        file.save(cache_url)
        img = Image.open(cache_url)
        img = np.array(img, dtype='uint8')
        img = Image.fromarray(img)
        # todo:这里根据需要做进一步的处理，比如reshape
        input.append(img)

        # img.save('.' + img_url)  # 测试代码

    # 得到输出
    assert len(input) > 0
    input = np.array(input, dtype='uint8')
    input_name = sess.get_inputs()[0].name
    outputs = sess.run(None, {input_name: input})

    # 分别保存图片
    for idx, output in outputs:
        output_img = Image.fromarray(output.astype(np.uint8))
        output_img.save(img_urls[idx])

    # 清除图片缓存
    for image_path in os.listdir(img_cache_path):
        os.remove(img_cache_path + image_path)
    print(img_urls)
    return json.dumps({'processed_image_urls': img_urls})

if __name__ == '__main__':
    app.run(debug=True)