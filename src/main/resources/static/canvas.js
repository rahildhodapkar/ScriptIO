const canvas = document.getElementById("canvas");
const context = canvas.getContext("2d");
context.fillStyle = 'White';
context.fillRect(0,0,canvas.width,canvas.height);
const clearBtn = document.getElementById("clearBtn");
const undoBtn = document.getElementById("undoBtn");
const execute_btn = document.getElementById("execute");
const execute_draw = document.getElementById("draw");
const execute_erase = document.getElementById("erase");

let timeoutId = null; // Initialize timeoutId to null
let painting = false;
const drawingHistory = [];

// Set up initial drawing properties
context.lineWidth = 2;
context.strokeStyle = "black";

// Event listeners
canvas.addEventListener("mousedown", startPaint);
canvas.addEventListener("mouseup", endPaint);
canvas.addEventListener("mousemove", draw);

clearBtn.addEventListener("click", clearCanvas);
undoBtn.addEventListener("click", undoLastStroke);
execute_btn.addEventListener("click", execute_process);

execute_draw.addEventListener("click", execute_draw_f);
execute_erase.addEventListener("click", execute_erase_f);

// Functions for drawing
function startPaint(event) {
    if (timeoutId) {
        clearTimeout(timeoutId); // Clear the existing timeout if any
        timeoutId = null; // Reset timeoutId to null
    }
    painting = true;
    draw(event);
}

function endPaint() {
    painting = false;
    context.beginPath();
    saveDrawingState();

    // Set a timeout to call execute_process after 3 seconds
    timeoutId = setTimeout(function() {
        execute_process();
        timeoutId = null; // Reset timeoutId to null
    }, 1700); // 3000 milliseconds = 3 seconds
}

function draw(event) {
    if (!painting) return;
    context.lineCap = "round";
    context.lineTo(event.clientX - canvas.getBoundingClientRect().left, event.clientY - canvas.getBoundingClientRect().top);
    context.stroke();
    context.beginPath();
    context.moveTo(event.clientX - canvas.getBoundingClientRect().left, event.clientY - canvas.getBoundingClientRect().top);
}

function clearCanvas() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    drawingHistory.length = 0; // Clear the drawing history
    context.fillStyle = 'White';
    context.fillRect(0,0,canvas.width,canvas.height);
}

function saveDrawingState() {
    const state = context.getImageData(0, 0, canvas.width, canvas.height);
    drawingHistory.push(state);
}

function undoLastStroke() {
    if (drawingHistory.length > 0) {
        drawingHistory.pop(); // Remove the last saved state
        context.clearRect(0, 0, canvas.width, canvas.height);
        if (drawingHistory.length > 0) {
            context.putImageData(drawingHistory[drawingHistory.length - 1], 0, 0); // Restore the previous state
        }
    }
}

function execute_process() {
    const pngDataURL = canvas.toDataURL('image/png');
    console.log(pngDataURL);
    fetch('/canvas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({image:pngDataURL})
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success', data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
    clearCanvas();
}

function execute_draw_f() {
    context.lineWidth = 2;
    context.strokeStyle = "Black";
}

function execute_erase_f() {
    context.lineWidth = 10;
    context.strokeStyle = "White";
}
