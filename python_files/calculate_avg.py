import time

def print_gyroscope_data(file_path='data.txt'):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    total_x = 0.0
    total_y = 0.0
    total_z = 0.0
    count = 0

    for line in lines:
        parts = line.split(',')
        x_part = parts[0].split(':')[1].strip()
        y_part = parts[1].split(':')[1].strip()
        z_part = parts[2].split(':')[1].strip()

        # Check if the parts are valid float values before converting
        try:
            x = float(x_part)
            y = float(y_part)
            z = float(z_part)

            total_x += x
            total_y += y
            total_z += z
            count += 1

        except ValueError:
            print(f"Invalid data format: {line.strip()}")

    if count > 0:
        average_x = total_x / count
        average_y = total_y / count
        average_z = total_z / count

        print(f"Average Gyroscope Data - \nX: {average_x:.5f}\nY: {average_y:.5f}\nZ: {average_z:.5f}")
        # print(f"Total Data Points: {count}")
    else:
        print("No valid data found in the file.")

if __name__ == "__main__":
    while True:
        print_gyroscope_data()
        time.sleep(2)
