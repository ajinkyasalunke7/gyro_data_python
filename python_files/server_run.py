from flask import Flask, request, jsonify

app = Flask(__name__)
x = []
y = []
z = []

@app.route('/gyroscope', methods=['POST'])
def gyroscope():
    data = request.get_json()
    x.append(data['x'])
    y.append(data['y'])
    z.append(data['z'])

    with open('data.txt', 'a') as file:
        file.write(f"X: {x[-1]}, Y: {y[-1]}, Z: {z[-1]}\n")

    # Process gyroscope data as needed (e.g., store in a list, perform calculations)
    # print(x, y, z)
    print(f"Gyroscope Data - X: {x[-1]}, Y: {y[-1]}, Z: {z[-1]}")

    return jsonify({"message": "Data received successfully"})

if __name__ == '__main__':
    # Change the host parameter to '0.0.0.0' to make it accessible externally
    app.run(debug=True, host='0.0.0.0')
