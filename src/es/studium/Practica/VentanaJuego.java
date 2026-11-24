package es.studium.Practica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class VentanaJuego extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int ANCHO = 900;
	public static final int ALTO = 600;

	public VentanaJuego()
	{
		setTitle("Práctica Tema 2 - Juego de Peces Multihilo");
		setSize(ANCHO, ALTO);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		PanelBienvenida bienvenida = new PanelBienvenida(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				iniciarJuego();
			}
		});

		setContentPane(bienvenida);
	}

	public void iniciarJuego()
	{
		PanelJuego panelJuego = new PanelJuego(this);
		setContentPane(panelJuego);
		revalidate();
		repaint();
		panelJuego.iniciarJuego();
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new VentanaJuego().setVisible(true);
			}
		});
	}
}