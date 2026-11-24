package es.studium.Practica;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Comida
{
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Image imagen;

	public Comida(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.ancho = 20;
		this.alto = 20;
		this.imagen = new ImageIcon("Recursos/Imagenes/comidapez.png").getImage();
	}

	public void dibujar(Graphics2D g2d)
	{
		g2d.drawImage(imagen, x, y, ancho, alto, null);
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x, y, ancho, alto);
	}
}