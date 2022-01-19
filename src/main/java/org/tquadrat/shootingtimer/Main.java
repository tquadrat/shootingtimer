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

import static java.lang.System.err;
import static javafx.application.Platform.exit;
import static javafx.scene.input.KeyCombination.NO_MATCH;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.i18n.I18nUtil.loadResourceBundle;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.DebugOutput.debugOutput;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.ProgramClass;
import org.tquadrat.foundation.exception.ApplicationError;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.fx.SceneUserData;
import org.tquadrat.foundation.i18n.BaseBundleName;
import org.tquadrat.foundation.i18n.I18nUtil;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.MessagePrefix;
import org.tquadrat.foundation.i18n.Translation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *  The main class for the Shooting Timer application.
 *
 *  @version $Id: Action.java 944 2021-12-21 21:56:24Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ProgramClass
@ClassVersion( sourceVersion = "$Id: Action.java 944 2021-12-21 21:56:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class Main extends Application
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The base bundle name, used for the texts and messages: {@value}.
     */
    @BaseBundleName
    public static final String BASE_BUNDLE_NAME = "TxtMsg";

    /**
     *  The message prefix for the messages: {@value}.
     */
    @MessagePrefix
    public static final String MESSAGE_PREFIX = "ST";

    /**
     *  Message: {@value}.
     */
    public static final String MSG_CannotLoadTextResources = "Unable to locate text resources";

    /**
     *  The message key for the message: &quot;Unable to load CSS
     *  stylesheet.&quot;.
     */
    @Message
    (
        description = "The resource with the CSS stylesheet cannot be found",
        translations =
            {
                @Translation( language = "de", text = "Das Stylesheet '%1$s' kann nicht geladen werden" ),
                @Translation( language = "en", text = "Unable to load the stylesheet '%1$s'" )
            }
    )
    public static final int MSGKEY_CannotLoadStylesheet = 1;

    /**
     *  The message key for the message: &quot;Unable to load UI
     *  definition.&quot;.
     */
    @Message
        (
            description = "The resource with the FXML cannot be found",
            translations =
                {
                    @Translation( language = "de", text = "Die UI-Definitionsdatei '%1$s' kann nicht geladen werden" ),
                    @Translation( language = "en", text = "Unable to load the UI definitions file '%1$s'" )
                }
        )
    public static final int MSGKEY_CannotLoadUIDefinition = 2;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    public static final void main( final String... args )
    {
        try
        {
            launch( args );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()

    /**
     *  <p>{@summary Retrieves the message with the given key from the given
     *  resource bundle and applies the given arguments to it.}</p>
     *  <p>If the resource bundle does not contain a message for the given key,
     *  the key itself will be returned, appended with the arguments.</p>
     *
     *  @param  bundle  The resource bundle.
     *  @param  messageKey  The id for the message.
     *  @param  args    The arguments for the message.
     *  @return The text.
     */
    public final String retrieveMessage( final ResourceBundle bundle, final int messageKey, final Object... args )
    {
        final var retValue = I18nUtil.retrieveMessage( requireNonNullArgument( bundle, "bundle" ), MESSAGE_PREFIX, messageKey, true, args );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveMessage()

    /**
     *  {@inheritDoc}
     *
     *  @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public final void start( final Stage primaryStage )
    {
        try
        {
            //---* Display the physical screen size *--------------------------
//            out.println( "Screen Size" );
//            final Screen screen = Screen.getPrimary();
//            final Rectangle2D primaryScreenBounds = screen.getBounds();
//            final Rectangle2D primaryScreenBoundsVisual = screen.getVisualBounds();
//            out.printf( "width  = %6.0f/%6.0f\n", primaryScreenBounds.getWidth(), primaryScreenBoundsVisual.getWidth() );
//            out.printf( "Min x  = %6.0f/%6.0f\n", primaryScreenBounds.getMinX(), primaryScreenBoundsVisual.getMinX() );
//            out.printf( "Max x  = %6.0f/%6.0f\n", primaryScreenBounds.getMaxX(), primaryScreenBoundsVisual.getMaxX() );
//            out.printf( "height = %6.0f/%6.0f\n", primaryScreenBounds.getHeight(), primaryScreenBoundsVisual.getHeight() );
//            out.printf( "Min y  = %6.0f/%6.0f\n", primaryScreenBounds.getMinY(), primaryScreenBoundsVisual.getMinY() );
//            out.printf( "Max y  = %6.0f/%6.0f\n", primaryScreenBounds.getMaxY(), primaryScreenBoundsVisual.getMaxY() );

            //---* Load the resource bundle with the texts *-------------------
            final var textURL = getClass().getResource( "/" + BASE_BUNDLE_NAME.replace( '.', '/' ) + ".properties" );
            //noinspection ConstantConditions
            debugOutput( nonNull( textURL ), $ -> textURL.toExternalForm() );
            debugOutput( isNull( textURL ), "Not found %s%n"::formatted, "/" + BASE_BUNDLE_NAME.replace( '.', '/' ) + ".properties" );

            ResourceBundle.getBundle( BASE_BUNDLE_NAME );

            final var resources = loadResourceBundle( BASE_BUNDLE_NAME )
                .orElseThrow( () -> new ApplicationError( MSG_CannotLoadTextResources ) );

            //---* Create the main window *------------------------------------
            final var fxmlFileName = "Main.fxml";
            final var fxmlURL = getClass().getResource( fxmlFileName );
            if( isNull( fxmlURL ) )
            {
                throw new ApplicationError( retrieveMessage( resources, MSGKEY_CannotLoadUIDefinition, fxmlFileName ) );
            }
            final BorderPane root = FXMLLoader.load( fxmlURL, resources );

            //---* Create the scene *------------------------------------------
            final var scene = SceneUserData.createScene( this, primaryStage, root );
            final var userdata = SceneUserData.retrieveUserData( scene, Main.class )
                .orElseThrow( () -> new UnexpectedExceptionError( new NoSuchElementException() ) );

            //---* Add the style sheet to the scene *--------------------------
            final var cssFileName = "application.css";
            final var cssURL = getClass().getResource( cssFileName );
            if( isNull( cssURL ) )
            {
                throw new ApplicationError( retrieveMessage( resources, MSGKEY_CannotLoadStylesheet, cssFileName ) );
            }
            scene.getStylesheets().add( cssURL.toExternalForm() );
            userdata.setApplicationCSS( cssURL );

            //---* Configure the stage *---------------------------------------
            primaryStage.centerOnScreen();
            primaryStage.setFullScreen( true );
            primaryStage.setFullScreenExitHint( EMPTY_STRING );
            primaryStage.setFullScreenExitKeyCombination( NO_MATCH );

            //---* Show the stage *--------------------------------------------
            primaryStage.show();
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
            exit();
        }
    }
    //  start()
}
//  class Main

/*
 *  End of File
 */