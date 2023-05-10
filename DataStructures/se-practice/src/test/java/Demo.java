

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.ChessPiece;
import project.pieces.*;
import project.ChessBoard;
import project.Game;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Image;


public class Demo{
	public static void main(String[] args) throws IOException{
		Game game = new Game("Morphy.pgn");
		game.manageGraphics();
	}
}