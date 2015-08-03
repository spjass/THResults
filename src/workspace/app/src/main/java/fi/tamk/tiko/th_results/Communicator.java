package fi.tamk.tiko.th_results;

import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Interface to communicate between fragments
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public interface Communicator {
    public void respond(Tournament tournament, int fragmentPosition, boolean isNew);
}
