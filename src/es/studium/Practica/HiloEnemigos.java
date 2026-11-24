package es.studium.Practica;

public class HiloEnemigos implements Runnable
{
	private PanelJuego panel;

	public HiloEnemigos(PanelJuego panel)
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
				Thread.sleep(1200); // Genera un pez enemigo cada 1,2 segundos
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}

			if (panel.isJugando())
			{
				panel.generarEnemigo();
			}
		}
	}
}