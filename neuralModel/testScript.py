import tensorflow as tf

def inspect_saved_model(saved_model_dir):
    # Load the SavedModel
    loaded = tf.saved_model.load(saved_model_dir)
    
    # Accessing the concrete function of the serving_default signature
    infer = loaded.signatures['serving_default']
    
    # Print the details of the input and output tensors
    print('Inputs:')
    for input_name, input_tensor in infer.structured_input_signature[1].items():
        print(f'{input_name}: {input_tensor}')
    
    print('\nOutputs:')
    for output_name, output_tensor in infer.structured_outputs.items():
        print(f'{output_name}: {output_tensor}')

# Specify the directory of your SavedModel
saved_model_dir = 'neuralModel/my_model'
inspect_saved_model(saved_model_dir)
