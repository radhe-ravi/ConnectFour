<h1> STEPS FOR RUNNING DOCKER IMAGE ON YOUR SYSTEM </h1>


 🪟 Running on linux
 - docker build -t <Image-Name> <docker-file-location>
 - xhost +local:
 - docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix <Image-Name>

 🪟 Running on Windows
✅ Requirements

    Docker Desktop (WSL2 backend)

    X Server installed on Windows (e.g., VcXsrv, Xming)

▶️ Setup X Server

    Open VcXsrv (or Xming)

    Use these settings:

        Multiple Windows

        Start no client

        Disable access control (for testing; restrict later for security)

    Launch

▶️ Set DISPLAY Variable

In PowerShell or CMD, run:

set DISPLAY=host.docker.internal:0.0

Or using PowerShell syntax:

$env:DISPLAY="host.docker.internal:0.0"

    Replace host.docker.internal with your machine's IP if needed (e.g., 192.168.1.100:0.0)

▶️ Run Docker Container

docker run -e DISPLAY=%DISPLAY% connect4

Or in PowerShell:

docker run -e DISPLAY=$env:DISPLAY connect4

🛠 Troubleshooting

    If the GUI does not appear:

        Ensure your firewall is not blocking X server

        Check if Docker can access the display (try echo $DISPLAY inside the container)

        On Windows, verify VcXsrv is running and allowing access
    
