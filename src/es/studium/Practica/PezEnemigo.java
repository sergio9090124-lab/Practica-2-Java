package es.studium.Practica;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class PezEnemigo
{
	private int x;
	private int y;
	private int tamano;
	private int velocidad;
	private boolean mirandoDerecha;
	private Image imagen;

	public PezEnemigo(int tamano, int anchoPanel, int altoPanel, boolean desdeIzquierda)
	{
		this.tamano = tamano;
		this.mirandoDerecha = desdeIzquierda;
		this.imagen = new ImageIcon("Recursos/Imagenes/pezenemigo.png").getImage();

		int ancho = getAncho();
		int alto = getAlto();

		// Altura aleatoria dentro de los límites verticales del panel
		int maxY = Math.max(10, altoPanel - alto - 10);
		this.y = (int) (Math.random() * maxY);

		// Velocidad entre 2 y 5 píxeles por fotograma
		int rapidez = 2 + (int) (Math.random() * 4);

		if (desdeIzquierda)
		{
			this.x = -ancho;
			this.velocidad = rapidez;
		}
		else
		{
			this.x = anchoPanel;
			this.velocidad = -rapidez;
		}
	}

	public void mover()
	{
		x += velocidad;
	}

	public boolean estaFuera(int anchoPanel)
	{
		if (mirandoDerecha)
		{
			return x > anchoPanel;
		}
		else
		{
			return x + getAncho() < 0;
		}
	}

	public void dibujar(Graphics2D g2d)
	{
		int ancho = getAncho();
		int alto = getAlto();

		if (mirandoDerecha)
		{
			g2d.drawImage(imagen, x, y, ancho, alto, null);
		}
		else
		{
			g2d.drawImage(imagen, x + ancho, y, -ancho, alto, null);
		}

		// Número de tamaño encima del pez enemigo
		g2d.setColor(Color.YELLOW);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		g2d.drawString(String.valueOf(tamano), x + (ancho / 2) - 6, y - 5);
	}

	public int getAncho()
	{
		return 20 + (tamano * 3);
	}

	public int getAlto()
	{
		return 14 + (tamano * 2);
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x, y, getAncho(), getAlto());
	}
	
	public Rectangle getHitbox()
	{
		int ancho = getAncho();
		int alto = getAlto();
		int margenX = (int) (ancho * 0.15);
		int margenY = (int) (alto * 0.15);
		return new Rectangle(x + margenX, y + margenY, ancho - (margenX * 2), alto - (margenY * 2));
	}

	public int getTamano()
	{
		return tamano;
	}
}