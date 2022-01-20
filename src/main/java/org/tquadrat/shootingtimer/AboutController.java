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

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *  The controller for the About window.
 *
 *  @version $Id: AboutController.java 106 2022-01-19 15:53:24Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: AboutController.java 106 2022-01-19 15:53:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class AboutController
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Initialises the controller.
     */
    @FXML
    private final void initialize()
    {
        /* Just exists */
    }   //  initialize()

    /**
     *  The handler for the OK button.
     *
     *  @param  event   The button event.
     */
    @SuppressWarnings( "static-method" )
    @FXML
    private final void onOk( final ActionEvent event )
    {
        event.consume();

        //---* Find the window to close *--------------------------------------
        /*
         * We know that the triggering event originates from a button - the
         * only button on the About dialog.
         * That button is part of the scene that is defined by the FXML file.
         * So we just walk up the scenes to the root, and from there we take
         * the Window (which is a Stage, something we know because the dialog
         * is shown in a Stage).
         */
        final var stage = (Stage) ((Button) event.getSource()).getScene()
            .getRoot()
            .getScene()
            .getWindow();
        stage.close();
    }   //  onOk()
}
//  class AboutController

/*
 *  End of File
 */