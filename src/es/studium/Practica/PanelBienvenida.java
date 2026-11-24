package es.studium.Practica;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PanelBienvenida extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JButton btnJugar;

	public PanelBienvenida(ActionListener accionJugar)
	{
		setLayout(new BorderLayout(15, 15));
		setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		JLabel lblTitulo = new JLabel("OCÉANO SUPERVIVENCIA", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
		add(lblTitulo, BorderLayout.NORTH);

		JTextArea txtInstrucciones = new JTextArea();
		txtInstrucciones.setEditable(false);
		txtInstrucciones.setLineWrap(true);
		txtInstrucciones.setWrapStyleWord(true);
		txtInstrucciones.setFont(new Font("Arial", Font.PLAIN, 15));
		txtInstrucciones.setOpaque(false);
		txtInstrucciones.setText(
			"MECÁNICA DEL JUEGO:\n\n" +
			"• Empiezas siendo un pez de tamaño 5.\n" +
			"• Come bolitas de comida (+1 de tamaño) o peces más pequeños que tú (sumas su tamaño).\n" +
			"• Si chocas contra un pez de igual o mayor tamaño que el tuyo, ¡serás devorado y perderás!\n" +
			"• Ganas la partida cuando alcances el tamaño 50."
		);
		add(txtInstrucciones, BorderLayout.CENTER);

		btnJugar = new JButton("Comenzar Partida");
		btnJugar.setFont(new Font("Arial", Font.BOLD, 16));
		btnJugar.addActionListener(accionJugar);
		add(btnJugar, BorderLayout.SOUTH);
	}
}