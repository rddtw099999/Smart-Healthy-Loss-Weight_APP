# Smart Loss Weight APP Client
*Designed by Minkai*

This project is to connect a weight loss device. When wearing this device while eating, it will send the data transmitted by the weight loss device to this application, and perform digital signal processing and get satiety through machine learning.

This Application currently Provide following functions:

> 1. Find Bluetooth devices
> 2. Connect to Smart-Loss-Weight device
> 3. Plot sound wave which received from device
> 4. Export RAW text data which received from device for analysis
> 5. Show time ,received data count and sample rate
> 6. Sample Rate up to 4000sps with low missing rate

Those functions may be provided in the future
> 1. Running machine learning models 
> 2. Estimate satiety
> 3. Show Short-Time Fourier transfer graph
> 4. Export Sound as wave file for analysis
> 5. Share with friend

Transmit and Receive Format:

    

> Smart Loss Weight Device sends 2 bytes data per sample
> First 4 bits are check bits  to prevent data loss .
> Last 12 bits are sound data ,which  transmitted from Bluetooth.
> ![enter image description here](https://i.imgur.com/It8bAKe.png)

User Interface:
![enter image description here](https://i.imgur.com/dlsfM7c.png)
![enter image description here](https://i.imgur.com/wc0qwib.png)
![enter image description here](https://i.imgur.com/h2SR00W.png)
![enter image description here](https://i.imgur.com/wRBF6iM.png)
![enter image description here](https://i.imgur.com/ByHYDGg.png)
![enter image description here](https://i.imgur.com/xB4gius.png)
