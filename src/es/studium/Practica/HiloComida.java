package es.studium.Practica;

public class HiloComida implements Runnable
{
	private PanelJuego panel;

	public HiloComida(PanelJuego panel)
	{
		this.panel = panel;
	}

	@Override
	public void run()
	{
		while (panel.isJugando())
		{
			try
			{
				Thread.sleep(1500); // Genera comida cada 1,5 segundos
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}

			if (panel.isJugando())
			{
				panel.generarComida();
			}
		}
	}
}