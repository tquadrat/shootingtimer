/*
 * ============================================================================
 * Copyright © 2002-2022 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.shootingtimer;

import static java.lang.System.currentTimeMillis;
import static javafx.application.Platform.runLater;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 *  The service that will update the time display.
 *
 *  @version $Id: TimeUpdateService.java 106 2022-01-19 15:53:24Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: TimeUpdateService.java 106 2022-01-19 15:53:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class TimeUpdateService extends ScheduledService<Void>
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The task that will update the time display.
     *
     *  @version $Id: TimeUpdateService.java 106 2022-01-19 15:53:24Z tquadrat $
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @UMLGraph.link
     *  @since 0.1.0
     */
    @ClassVersion( sourceVersion = "$Id: TimeUpdateService.java 106 2022-01-19 15:53:24Z tquadrat $" )
    @API( status = INTERNAL, since = "0.1.0" )
    private class TimeUpdateTask extends Task<Void>
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code TimeUpdateTask} instance.
         */
        public TimeUpdateTask() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final Void call()
        {
            final var currentTime = currentTimeMillis();
            final var remainingTime = currentTime < m_EndTime ? m_EndTime - currentTime : 0L;
            if( m_ShowMinutes )
            {
                runLater( () ->
                    {
                        final var minutes = remainingTime / 60_000;
                        final var seconds = (remainingTime / 1000) % 60;
                        m_TimeDisplay.setText( "%d:%02d".formatted( minutes, seconds ) );
                    } );
            }
            else
            {
                runLater( () -> m_TimeDisplay.setText( "%3.1f".formatted( remainingTime / 1000.0 ) ) );
            }
            if( (remainingTime <= 0L) || m_Stop ) cancel();

            //---* Done *------------------------------------------------------
            return null;
        }   //  call()
    }
    //  class TimeUpdateTask

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The end time.
     */
    private final long m_EndTime;

    /**
     *  Flag that indicates if either minutes or seconds should be
     *  displayed.
     */
    private final boolean m_ShowMinutes;

    /**
     *  The flag that stops the service.
     */
    private boolean m_Stop = false;

    /**
     *  The reference for the node that displays the time.
     */
    private final TextField m_TimeDisplay;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code TimeUpdateService} instance.
     *
     *  @param  endTime The end time.
     *  @param  timeDisplay The reference for the node that displays the time.
     */
    public TimeUpdateService( final long endTime, final TextField timeDisplay )
    {
        m_EndTime = endTime;
        m_TimeDisplay = requireNonNullArgument( timeDisplay, "timeDisplay" );
        m_ShowMinutes = endTime - currentTimeMillis() > 350_000;
        setPeriod( new Duration( 100 ) );
    }   //  TimeUpdateService()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Stops the service.
     */
    public final void stop() { m_Stop = true; }

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Task<Void> createTask() { return new TimeUpdateTask(); }
}
//  class TimeUpdateService

/*
 *  End of File
 */