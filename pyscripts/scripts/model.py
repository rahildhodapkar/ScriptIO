import tensorflow as tf
from emnist import extract_training_samples
import numpy as np
import sklearn
from sklearn.model_selection import train_test_split
from PIL import Image
import matplotlib.pyplot as plt
from matplotlib import image

def data_prep():
    images, labels = extract_training_samples('byclass')
    images = np.array(images)
    images.reshape(images.shape[0], images.shape[1], images.shape[2], 1)

    data_train, data_test, labels_train, labels_test = train_test_split(images, labels, test_size=0.20, random_state=42)
    data_train, data_test = data_train / 255.0, data_test / 255.0

    train_dataset = tf.data.Dataset.from_tensor_slices((data_train, labels_train))
    test_dataset = tf.data.Dataset.from_tensor_slices((data_test, labels_test))

def make_tf_dataset(batch_size=64, shuffle_buffer_size=100):
    BATCH_SIZE = batch_size
    SHUFFLE_BUFFER_SIZE = 100

    train_dataset = train_dataset.shuffle(SHUFFLE_BUFFER_SIZE).batch(BATCH_SIZE)
    test_dataset = test_dataset.batch(BATCH_SIZE)
    return train_dataset, test_dataset

model = tf.keras.Sequential([
    tf.keras.Input(shape=(28, 28, 1)),
    tf.keras.layers.Conv2D(32, kernel_size=(3,3), activation="relu"),
    tf.keras.layers.MaxPooling2D(pool_size=(2,2)),
    tf.keras.layers.Conv2D(64, kernel_size=(3,3), activation="relu"),
    tf.keras.layers.MaxPooling2D(pool_size=(2,2)),
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dropout(0.5),
    tf.keras.layers.Dense(62, activation="softmax"),
])
'''
model = tf.keras.Sequential([
    tf.keras.layers.Flatten(input_shape=(28, 28)),
    tf.keras.layers.Dense(128, activation='relu'),
    tf.keras.layers.Dropout(0.1),
    tf.keras.layers.Dense(62, activation='softmax')
])
'''

def train(model, save=True, savepath=True):
    model.compile(optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy'])
    model.fit(train_dataset, epochs=5)

def test(model):
    model.evaluate(test_dataset)

def predict(model, img_path):
    img = Image.open(img_path).convert('L')
    img = img.resize((28, 28))
    img = np.asarray(img)
    img.reshape(28, 28, 1)
    if model != None:
        pred = model.predict(img)
        return np.argmax(pred)