package es.studium.Practica;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelJuego extends JPanel implements Runnable, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private VentanaJuego ventana;
	private Image fondo;
	private PezJugador jugador;
	private List<Comida> listaComida;
	private List<PezEnemigo> listaEnemigos;
	private ReproductorSonido reproductor;

	private Thread hiloJuego;
	private Thread hiloComida;
	private Thread hiloEnemigos;
	private volatile boolean jugando;

	public PanelJuego(VentanaJuego ventana)
	{
		this.ventana = ventana;
		addMouseMotionListener(this);

		fondo = new ImageIcon("Recursos/Imagenes/fondomarino.png").getImage();
		jugador = new PezJugador(VentanaJuego.ANCHO / 2 - 20, VentanaJuego.ALTO / 2 - 15);
		listaComida = new CopyOnWriteArrayList<>();
		listaEnemigos = new CopyOnWriteArrayList<>();
		reproductor = new ReproductorSonido();
		jugando = true;
	}

	public void iniciarJuego()
	{
		reproductor.iniciarMusicaFondo("Recursos/Sonidos/musica.wav");

		hiloJuego = new Thread(this);
		hiloComida = new Thread(new HiloComida(this));
		hiloEnemigos = new Thread(new HiloEnemigos(this));

		hiloJuego.start();
		hiloComida.start();
		hiloEnemigos.start();
	}

	public void generarComida()
	{
		int ancho = getWidth() > 0 ? getWidth() : VentanaJuego.ANCHO;
		int alto = getHeight() > 0 ? getHeight() : VentanaJuego.ALTO;

		if (listaComida.size() < 15)
		{
			int x = 30 + (int) (Math.random() * (ancho - 60));
			int y = 40 + (int) (Math.random() * (alto - 80));
			listaComida.add(new Comida(x, y));
		}
	}

	public void generarEnemigo()
	{
		int ancho = getWidth() > 0 ? getWidth() : VentanaJuego.ANCHO;
		int alto = getHeight() > 0 ? getHeight() : VentanaJuego.ALTO;

		int tamJugador = jugador.getTamano();
		int tamEnemigo;

		if (Math.random() < 0.5)
		{
			int maxPequeno = Math.max(1, tamJugador - 1);
			tamEnemigo = 1 + (int) (Math.random() * maxPequeno);
		}
		else
		{
			tamEnemigo = tamJugador + (int) (Math.random() * 25);
		}

		boolean desdeIzquierda = Math.random() < 0.5;
		listaEnemigos.add(new PezEnemigo(tamEnemigo, ancho, alto, desdeIzquierda));
	}

	@Override
	public void run()
	{
		while (jugando)
		{
			int ancho = getWidth();
			int alto = getHeight();

			jugador.mover(ancho, alto);

			for (PezEnemigo enemigo : listaEnemigos)
			{
				enemigo.mover();
				if (enemigo.estaFuera(ancho))
				{
					listaEnemigos.remove(enemigo);
				}
			}

			comprobarColisiones();
			repaint();

			try
			{
				Thread.sleep(16); // ~60 FPS
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}
	}

	private void comprobarColisiones()
	{
		Rectangle hitboxJugador = jugador.getHitbox();

		// 1. Colisión con comida (+1 de tamaño)
		for (Comida c : listaComida)
		{
			if (hitboxJugador.intersects(c.getBounds()))
			{
				listaComida.remove(c);
				jugador.sumarTamano(1);
				ReproductorSonido.reproducirEfecto("Recursos/Sonidos/comer.wav");

				if (jugador.getTamano() >= 50)
				{
					finalizarPartida(true);
					return;
				}
			}
		}

		// 2. Colisión con peces enemigos
		for (PezEnemigo enemigo : listaEnemigos)
		{
			if (hitboxJugador.intersects(enemigo.getHitbox()))
			{
				if (enemigo.getTamano() < jugador.getTamano())
				{
					// El jugador se come al pez más pequeño y suma su tamaño
					listaEnemigos.remove(enemigo);
					jugador.sumarTamano(enemigo.getTamano());
					ReproductorSonido.reproducirEfecto("Recursos/Sonidos/comer.wav");

					if (jugador.getTamano() >= 50)
					{
						finalizarPartida(true);
						return;
					}
				}
				else
				{
					// Un pez igual o más grande se come al jugador
					finalizarPartida(false);
					return;
				}
			}
		}
	}

	private void finalizarPartida(boolean victoria)
	{
		if (!jugando)
		{
			return;
		}
		jugando = false;

		reproductor.detenerMusicaFondo();
		if (victoria)
		{
			ReproductorSonido.reproducirEfecto("Recursos/Sonidos/win.wav");
		}
		else
		{
			ReproductorSonido.reproducirEfecto("Recursos/Sonidos/derrota.wav");
		}

		// Interrumpimos los hilos generadores para que cierren 
		if (hiloComida != null) hiloComida.interrupt();
		if (hiloEnemigos != null) hiloEnemigos.interrupt();

		repaint();

		String titulo = victoria ? "¡VICTORIA!" : "HAS SIDO DEVORADO";
		String mensaje = victoria
				? "¡Enhorabuena! Has alcanzado el tamaño " + jugador.getTamano() + " y dominas el océano."
				: "Te ha comido un pez igual o más grande que tú.\nTamaño alcanzado: " + jugador.getTamano() + " / 50";

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Object[] opciones = { "Volver a jugar", "Salir" };
				int eleccion = JOptionPane.showOptionDialog(
						ventana,
						mensaje,
						titulo,
						JOptionPane.YES_NO_OPTION,
						victoria ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE,
						null,
						opciones,
						opciones[0]
				);

				if (eleccion == JOptionPane.YES_OPTION)
				{
					ventana.iniciarJuego();
				}
				else
				{
					System.exit(0);
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// 1. Fondo marino
		g2d.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

		// 2. Comida
		for (Comida c : listaComida)
		{
			c.dibujar(g2d);
		}

		// 3. Peces enemigos
		for (PezEnemigo enemigo : listaEnemigos)
		{
			enemigo.dibujar(g2d);
		}

		// 4. Pez jugador
		jugador.dibujar(g2d);

		// 5. Marcador superior izquierdo
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 18));
		g2d.drawString("Tamaño: " + jugador.getTamano() + " / 50", 20, 30);
	}

	public boolean isJugando()
	{
		return jugando;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		jugador.setObjetivoRaton(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		jugador.setObjetivoRaton(e.getX(), e.getY());
	}
}