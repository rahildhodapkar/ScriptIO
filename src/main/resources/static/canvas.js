// JavaScript code for drawing on the canvas with an "Undo" button
const canvas = document.getElementById("canvas");
const context = canvas.getContext("2d");
const clearBtn = document.getElementById("clearBtn");
const undoBtn = document.getElementById("undoBtn");
const execute_btn = document.getElementById("execute")
const execute_draw = document.getElementById("draw")
const execute_erase = document.getElementById("erase")


let painting = false;
let lastX, lastY;
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

execute_draw.addEventListener("click",execute_draw_f)
execute_erase.addEventListener("click",execute_erase_f)

// Functions for drawing
function startPaint(event) {
    painting = true;
    draw(event);
}

function endPaint() {
    painting = false;
    context.beginPath();
    // Save the current drawing state to the history
    saveDrawingState();
    
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

function execute_process(){
    
    const data = JSON.stringify(drawingHistory.pop());
    console.log(data)

    
}

function execute_draw_f(){
    context.lineWidth = 2;
    context.strokeStyle = "Black";
}
function execute_erase_f(){
    context.lineWidth = 10;
    context.strokeStyle = "White";

}