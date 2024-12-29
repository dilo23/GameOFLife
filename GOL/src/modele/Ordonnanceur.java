package modele;


public class Ordonnanceur extends Thread {

    public long sleepTime;
    private Runnable runnable;
    public boolean enPause = false; // État de pause
    public Ordonnanceur(long _sleepTime, Runnable _runnable) {
        sleepTime = _sleepTime;
        runnable = _runnable;

    }

    public synchronized void setPause(boolean pause) {
        enPause = pause;
        notify(); // Réveille le thread s'il est en pause
    }

    public void run() {
        while (true) {
            synchronized (this) {
                while (enPause) {
                    try {
                        wait(); // Attente tant que le jeu est en pause
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            runnable.run();
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
