package es.studium.Practica;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class PezJugador
{
	private int x;
	private int y;
	private int ratonX;
	private int ratonY;
	private int tamano;
	private boolean mirandoDerecha;
	private Image imagen;

	public PezJugador(int xInicial, int yInicial)
	{
		this.x = xInicial;
		this.y = yInicial;
		this.tamano = 5; // Tamaño inicial
		this.ratonX = xInicial + (getAncho() / 2);
		this.ratonY = yInicial + (getAlto() / 2);
		this.mirandoDerecha = true;
		this.imagen = new ImageIcon("Recursos/Imagenes/pezamigo.png").getImage();
	}

	public void mover(int anchoPanel, int altoPanel)
	{
		int anchoPez = getAncho();
		int altoPez = getAlto();

		// Centramos el objetivo respecto al puntero del ratón
		int destinoX = ratonX - (anchoPez / 2);
		int destinoY = ratonY - (altoPez / 2);

		int diffX = destinoX - x;
		int diffY = destinoY - y;

		// Volteamos el sprite según hacia dónde nade
		if (diffX > 2)
		{
			mirandoDerecha = true;
		}
		else if (diffX < -2)
		{
			mirandoDerecha = false;
		}

		// Desplazamiento hacia el ratón
		x += (int) Math.round(diffX * 0.2);
		y += (int) Math.round(diffY * 0.2);

		// Evitar que se salga de los bordes del panel
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (anchoPanel > 0 && x + anchoPez > anchoPanel) x = anchoPanel - anchoPez;
		if (altoPanel > 0 && y + altoPez > altoPanel) y = altoPanel - altoPez;
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
			// Invierte horizontalmente la imagen al nadar hacia la izquierda
			g2d.drawImage(imagen, x + ancho, y, -ancho, alto, null);
		}

		// Muestra el tamaño actual encima del pez
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		g2d.drawString(String.valueOf(tamano), x + (ancho / 2) - 6, y - 5);
	}

	public void setObjetivoRaton(int ratonX, int ratonY)
	{
		this.ratonX = ratonX;
		this.ratonY = ratonY;
	}

	// Escala visual proporcional al tamaño lógico (empieza en 35x24 px con tamaño 5)
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

	public void sumarTamano(int incremento)
	{
		this.tamano += incremento;
	}
}