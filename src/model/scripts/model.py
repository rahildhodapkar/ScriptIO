import tensorflow as tf
from emnist import extract_training_samples
import numpy as np
import sklearn
from sklearn.model_selection import train_test_split

images, labels = extract_training_samples('byclass')
images = np.array(images)

data_train, data_test, labels_train, labels_test = train_test_split(images, labels, test_size=0.20, random_state=42)
data_train, data_test = data_train / 255.0, data_test / 255.0

train_dataset = tf.data.Dataset.from_tensor_slices((data_train, labels_train))
test_dataset = tf.data.Dataset.from_tensor_slices((data_test, labels_test))

BATCH_SIZE = 64
SHUFFLE_BUFFER_SIZE = 100

train_dataset = train_dataset.shuffle(SHUFFLE_BUFFER_SIZE).batch(BATCH_SIZE)
test_dataset = test_dataset.batch(BATCH_SIZE)

model = tf.keras.Sequential([
    tf.keras.layers.Flatten(input_shape=(28, 28)),
    tf.keras.layers.Dense(128, activation='relu'),
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(62, activation='softmax')
])

model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

model.fit(train_dataset, epochs=10)
model.evaluate(test_dataset)