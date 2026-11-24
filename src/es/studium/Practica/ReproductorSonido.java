package es.studium.Practica;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ReproductorSonido
{
	private Clip clipFondo;

	// Inicia la música de fondo en bucle continuo desde un hilo separado
	public void iniciarMusicaFondo(String rutaArchivo)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					detenerMusicaFondo();
					File archivo = new File(rutaArchivo);
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
					clipFondo = AudioSystem.getClip();
					clipFondo.open(audioStream);
					clipFondo.loop(Clip.LOOP_CONTINUOUSLY);
					clipFondo.start();
				}
				catch (Exception e)
				{
					System.err.println("No se pudo reproducir la música de fondo (" + rutaArchivo + "): " + e.getMessage());
				}
			}
		}).start();
	}

	// Detiene y libera la música de fondo al terminar la partida
	public void detenerMusicaFondo()
	{
		if (clipFondo != null)
		{
			if (clipFondo.isRunning())
			{
				clipFondo.stop();
			}
			clipFondo.close();
			clipFondo = null;
		}
	}

	// Reproduce un efecto instantáneo en su propio hilo y libera el recurso al acabar
	public static void reproducirEfecto(String rutaArchivo)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					File archivo = new File(rutaArchivo);
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
					Clip clip = AudioSystem.getClip();
					clip.open(audioStream);

					clip.addLineListener(new LineListener()
					{
						@Override
						public void update(LineEvent event)
						{
							if (event.getType() == LineEvent.Type.STOP)
							{
								clip.close();
							}
						}
					});

					clip.start();
				}
				catch (Exception e)
				{
					System.err.println("No se pudo reproducir el efecto (" + rutaArchivo + "): " + e.getMessage());
				}
			}
		}).start();
	}
}