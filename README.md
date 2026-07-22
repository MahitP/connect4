Connect4
A two-player Connect 4 game built in Java Swing with a fully custom-rendered board. All graphics are hand-drawn using Graphics2D — the board, discs, hover previews, and win highlighting are painted directly onto a JPanel.

## 🚀 Overview
A desktop implementation of Connect 4 that focuses on custom GUI rendering. Players take turns dropping discs into a 7x6 grid, and the game automatically detects horizontal, vertical, and diagonal wins. It features dynamic board scaling, custom name entry, and a transparent hover preview indicating where a piece will land.

## ✨ Features
- Custom 2D board and disc rendering
- Alpha-composited transparent hover preview
- Multi-directional win detection (horizontal, vertical, diagonals)
- Post-game winning cell highlighting with green borders
- Keyboard-controlled board translation (arrow keys) and scaling
- Custom player naming via modal dialogs

## ⭐ Technical Highlights
- **Custom Graphics Rendering**: Bypassed standard Swing UI widgets to paint the entire game state directly using `Graphics2D`.
- **Coordinate Mapping**: Implemented math to map pixel coordinates from mouse events back to logical grid indices.
- **Alpha Compositing**: Used `AlphaComposite.getInstance()` to render semi-transparent preview discs dynamically based on mouse position.

## 🛠️ Technical Implementation
The application extends `JPanel` and overrides `paintComponent` to draw the game state on every frame. The board is represented as a 2D integer array (`int[][] grid`). Mouse movement triggers `mouseMoved` events, calculating the current column via pixel-to-grid math to render the hover preview. `AlphaComposite` is applied to the `Graphics2D` context to make the preview semi-transparent. On a mouse click, the disc "drops" to the lowest available row in the selected column. Win detection runs after every move, checking four directions from the board cells to find four matching contiguous pieces.

## 🧠 Design Decisions & Challenges
- **Custom Painting vs. Swing Components**: Using raw `Graphics2D` instead of a grid of `JButton` components provided full control over the visual appearance and allowed for continuous custom drawing, such as the translucent hover effect and specific win highlighting.
- **Hover Preview Implementation**: To show the preview without affecting the game state, the `paintComponent` method checks the current `hoverCol` variable (updated by `MouseMotionListener`) and temporarily applies an alpha channel to the graphics context before painting the preview disc.
- **Dynamic Board Scaling**: Added keyboard listeners for zooming and panning the board to test rendering logic across different display sizes and ensure the coordinate mapping equations scaled properly.

## 📁 Project Structure
- `src/Connect4.java`: Main application file containing the game loop, UI rendering, event listeners, and win detection logic.

## ▶️ Running the Project
1. Ensure you have the Java Development Kit (JDK) installed.
2. Clone the repository:
   `git clone https://github.com/MahitP/connect4.git`
3. Navigate to the source directory:
   `cd connect4/src`
4. Compile the Java file:
   `javac Connect4.java`
5. Run the game:
   `java Connect4`

## 🔮 Future Improvements
- AI opponent using the Minimax algorithm
- Animated disc dropping
- Networked online multiplayer
- Undo move functionality

## Author
Mahit Pulavarthi — [github.com/MahitP](https://github.com/MahitP)
