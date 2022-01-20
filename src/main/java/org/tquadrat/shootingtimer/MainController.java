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
import static java.lang.System.err;
import static java.lang.System.getProperty;
import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static javafx.application.Platform.exit;
import static javafx.application.Platform.runLater;
import static javafx.stage.StageStyle.UTILITY;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.fx.SceneUserData.retrieveUserData;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_CPUARCHITECTURE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.shootingtimer.Discipline.DISCIPLINE_2_17;
import static org.tquadrat.shootingtimer.Main.MSGKEY_CannotLoadUIDefinition;
import static org.tquadrat.shootingtimer.Status.STATUS_AFTER;
import static org.tquadrat.shootingtimer.Status.STATUS_BEFORE;
import static org.tquadrat.shootingtimer.Status.STATUS_OFF;
import static org.tquadrat.shootingtimer.Status.STATUS_PROLOG;
import static org.tquadrat.shootingtimer.Status.STATUS_SHOOTING;
import static org.tquadrat.shootingtimer.Status.STATUS_STARTING;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ApplicationError;
import org.tquadrat.foundation.exception.UnsupportedEnumError;
import org.tquadrat.foundation.fx.SceneUserData;
import org.tquadrat.foundation.lang.AutoLock;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *  The main controller for the Shooting Timer application.
 *
 *  @version $Id: MainController.java 106 2022-01-19 15:53:24Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: MainController.java 106 2022-01-19 15:53:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class MainController
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  This class will manage the status for the lights.
     *
     *  @version $Id: MainController.java 106 2022-01-19 15:53:24Z tquadrat $
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @UMLGraph.link
     *  @since 0.1.0
     */
    @ClassVersion( sourceVersion = "$Id: MainController.java 106 2022-01-19 15:53:24Z tquadrat $" )
    @API( status = INTERNAL, since = "0.1.0" )
    private static class Light
    {
            /*-----------*\
        ====** Constants **====================================================
            \*-----------*/
        /**
         *  The color for the OFF state.
         */
        private final Color m_OffColor = Color.GRAY;

            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The method that is used to set the colour for the light.
         */
        private final Consumer<Color> m_ColorChanger;

        /**
         *  The color for the ON state.
         */
        private final Color m_OnColor;

        /**
         *  The current status; {@code true} mean ON, {@code false} is OFF.
         */
        private boolean m_Status = false;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code Light} instance.
         *
         *  @param  onColor The colour for the ON state.
         *  @param  colorChanger    The method that is used to set the colour
         *      for the light.
         */
        public Light( final Color onColor, final Consumer<Color> colorChanger )
        {
            m_ColorChanger = requireNonNullArgument( colorChanger, "colorChanger" );
            m_OnColor = requireNonNullArgument( onColor, "onColor" );
        }   //  Light()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Switches the light off.
         */
        public final void off()
        {
            m_Status = false;
            m_ColorChanger.accept( m_OffColor );
        }   //  off()

        /**
         *  Switches the light off.
         */
        public final void on()
        {
            m_Status = true;
            m_ColorChanger.accept( m_OnColor );
        }   //  off()

        /**
         *  Toggles the light.
         */
        public final void toggle()
        {
            if( m_Status )
            {
                off();
            }
            else
            {
                on();
            }
        }   //  toggle()
    }
    //  class Light

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name for the preference node that stores the last used discipline:
     *  {@value}.
     */
    public final static String PREF_NODE_Discipline = "/Discipline/Name";

    /**
     *  The name for the preference node that stores the last used heat for the
     *  current discipline: {@value}.
     */
    public final static String PREF_NODE_Heat = "/Discipline/Heat";

    /**
     *  The name for the preference node that stores the sound flag: {@value}.
     */
    public final static String PREF_NODE_Sound = "/Sound";

    /**
     *  The name for the preference node that stores the show heat count flag:
     *  {@value}.
     */
    public final static String PREF_NODE_ShowHeatCount = "/Show/HeatCount";

    /**
     *  The name for the preference node that stores the &quot;show time&quot;
     *  flag: {@value}.
     */
    public final static String PREF_NODE_ShowStatus = "/Show/Status";

    /**
     *  The name for the preference node that stores the &quot;show time&quot;
     *  flag: {@value}.
     */
    public final static String PREF_NODE_ShowTime = "/Show/Time";

    /**
     *  The name for the preference node that stores the time after shooting
     *  and before a reset: {@value}.
     */
    public final static String PREF_NODE_TimeAfter = "/Time/After";

    /**
     *  The name for the preference node that stores the time before the
     *  prolog: {@value}.
     */
    public final static String PREF_NODE_Before = "/Time/Before";

    /**
     *  The file name for the end sound: {@value}.
     */
    public final static String SOUND_ENDSHOOTING = "chinese-gong-daniel_simon.mp3";

    /**
     *  The file name for the start sound: {@value}.
     */
    public final static String SOUND_STARTSHOOTING = "foghorn-daniel_simon.mp3";

    /**
     *  The height for the fields in the status bar: {@value}.
     */
    @SuppressWarnings( "unused" )
    public final static double STATUSBAR_HEIGHT = 30.0;

    /**
     *  The height of the start button in the timer box: {@value}.
     */
    @SuppressWarnings( "unused" )
    public final static double TIMERBOX_STARTBUTTON_HEIGHT = 100.0;

    /**
     *  The height of the time text field in the timer box: {@value}.
     */
    @SuppressWarnings( "unused" )
    public final static double TIMERBOX_TIME_HEIGHT = 100.0;

    /**
     *  The width for the fields in the timer box: {@value}.
     */
    @SuppressWarnings( "unused" )
    public final static double TIMERBOX_WIDTH = 200.0;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The button that aborts a running heat.
     */
    @FXML
    private Button m_Button_Abort;

    /**
     *  The button that starts a heat.
     */
    @FXML
    private Button m_Button_Start;

    /**
     *  The pane that holds the buttons.
     */
    @FXML
    private Pane m_ButtonPane;

    /**
     *  The menu item for the heat count display.
     */
    @FXML
    private CheckMenuItem m_CheckMenuItem_ShowHeatCount;

    /**
     *  The menu item for the status display.
     */
    @FXML
    private CheckMenuItem m_CheckMenuItem_ShowStatus;

    /**
     *  The menu item for the time display.
     */
    @FXML
    private CheckMenuItem m_CheckMenuItem_ShowTime;

    /**
     *  The menu item for the sound control.
     */
    @FXML
    private CheckMenuItem m_CheckMenuItem_Sound;

    /**
     *  The choice box for the program of the selected discipline.
     */
    @FXML
    private ChoiceBox<Heat> m_ChoiceBox_Program;

    /**
     *  The circle for the green light.
     */
    @FXML
    private Circle m_Circle_Green;

    /**
     *  The circle for the red light.
     */
    @FXML
    private Circle m_Circle_Red;

    /**
     *  The toggle group for the selected discipline.
     */
    @FXML
    private ToggleGroup m_DisciplinesToggleGroup;

    /**
     *  The end sound.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Optional<AudioClip> m_EndSound;

    /**
     *  The green light.
     */
    private Light m_GreenLight;

    /**
     *  The thread that executes the current heat.
     */
    private Thread m_HeatThread;

    /**
     *  The guard for the heat thread.
     */
    private final AutoLock m_HeatThreadGuard;

    /**
     *  The label that displays the heat counter.
     */
    @FXML
    private Label m_Label_HeatCount;

    /**
     *  The label that shows the current system status.
     */
    @FXML
    private Label m_Label_Status;

    /**
     *  The label that shows the currently selected discipline.
     */
    @FXML
    private Label m_Label_SelectedDiscipline;

    /**
     *  The pane that holds the lights.
     */
    @FXML
    private GridPane m_LightBox;

    /**
     *  The preferences.
     */
    private final Preferences m_Preferences;

    /**
     *  The red light.
     */
    private Light m_RedLight;

    /**
     *  The currently selected discipline.
     */
    private final ObjectProperty<Discipline> m_SelectedDiscipline = new SimpleObjectProperty<>();

    /**
     *  The slider for the time after shooting and before the reset.
     */
    @FXML
    private Slider m_Slider_AfterTime;

    /**
     *  The slider for the time before the prolog.
     */
    @FXML
    private Slider m_Slider_BeforeTime;

    /**
     *  The start sound.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Optional<AudioClip> m_StartSound;

    /**
     *  The status control property.
     */
    private final ObjectProperty<Status> m_StatusControl;

    /**
     *  The guard for the status control.
     */
    private final AutoLock m_StatusControlGuard;

    /**
     *  The text field that is used to display the remaining time.
     */
    @FXML
    private TextField m_TextField_Time;

    /**
     *  The injected resource bundle.
     *
     *  @note   The name of the field is dictated by JavaFX.
     */
    @FXML
    private ResourceBundle resources;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code MainController} instance.
     */
    public MainController()
    {
        //---* Load the preferences *------------------------------------------
        m_Preferences = Preferences.userNodeForPackage( getClass() );
        m_Preferences.addPreferenceChangeListener( this::preferenceChanged );

        //---* Create the lock *-----------------------------------------------
        var lock = new ReentrantLock( false );
        m_HeatThreadGuard = AutoLock.of( lock );
        lock = new ReentrantLock( false );
        m_StatusControlGuard = AutoLock.of( lock );

        //---* Create the status control *-------------------------------------
        m_StatusControl = new SimpleObjectProperty<>();
        m_StatusControl.addListener( this::statusTransition );
    }   //  MainController()


        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Executes the current heat.
     */
    @SuppressWarnings( "BusyWait" )
    private final void executeHeat()
    {
        var proceed = !interrupted();

        //---* Retrieve the current heat settings *----------------------------
        final var heat = m_ChoiceBox_Program.getValue();

        //---* Execute ... *---------------------------------------------------
        setStatusStarting();
        proceedToNextStatus();
        final var beforeTime = (long) (m_Slider_BeforeTime.getValue() * 1000.0);
        if( beforeTime > 0.0 )
        {
            try
            {
                sleep( beforeTime );
            }
            catch( @SuppressWarnings( "unused" ) final InterruptedException e )
            {
                proceed = false;
            }
        }
        if( !heat.isRepeated() ) proceedToNextStatus();

        final var heatCount = new AtomicInteger();

        RepeatLoop: for( var r = 0; (r < heat.getRepetitions()) && proceed; ++r )
        {
            if( heat.isRepeated() ) setStatusProlog();

            //---* Update the heat count *-------------------------------------
            heatCount.set( r + 1 );
            runLater( () -> m_Label_HeatCount.setText( Integer.toString( heatCount.get() ) ) );

            //---* Start the prolog *------------------------------------------
            var endTime = currentTimeMillis() + heat.getPrologTime();
            var scheduledService = new TimeUpdateService( endTime, m_TextField_Time );
            scheduledService.start();
            PrologLoop: while( currentTimeMillis() < endTime )
            {
                if( interrupted() )
                {
                    proceed = false;
                    break PrologLoop;
                }
                try
                {
                    sleep( endTime - currentTimeMillis() );
                }
                catch( @SuppressWarnings( "unused" ) final InterruptedException e )
                {
                    proceed = false;
                    break PrologLoop;
                }
            }   //  PrologLoop:

            scheduledService.stop();
            proceedToNextStatus();

            if( proceed )
            {
                //---* Start the shooting *------------------------------------
                endTime = currentTimeMillis() + heat.getShootingTime();
                scheduledService = new TimeUpdateService( endTime, m_TextField_Time );
                scheduledService.start();
                ShootingLoop: while( currentTimeMillis() < endTime )
                {
                    if( interrupted() )
                    {
                        proceed = false;
                        break ShootingLoop;
                    }
                    try
                    {
                        sleep( endTime - currentTimeMillis() );
                    }
                    catch( @SuppressWarnings( "unused" ) final InterruptedException e )
                    {
                        proceed = false;
                        break ShootingLoop;
                    }
                }   //  ShootingLoop:
                scheduledService.stop();
            }

            proceed &= !interrupted();
        }   //  RepeatLoop:

        if( proceed )
        {
            //---* After shooting *--------------------------------------------
            proceedToNextStatus();
            final var epilogTime = (long) (m_Slider_AfterTime.getValue() * 1_000.0);
            if( epilogTime > 0.0 )
            {
                try
                {
                    sleep( epilogTime );
                }
                catch( @SuppressWarnings( "unused" ) final InterruptedException e ) { /* Deliberately ignored */ }
            }
        }

        //---* Cleanup *-------------------------------------------------------
        m_HeatThread = null;
        reset();
    }   //  executeHeat()

    /**
     *  Initialises the controller.
     */
    @FXML
    private final void initialize()
    {
        assert nonNull( m_Button_Abort ) : "fx:id=\"m_Button_Abort\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Button_Start ) : "fx:id=\"m_Button_Start\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_ButtonPane ) : "fx:id=\"m_ButtonPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_CheckMenuItem_ShowHeatCount ) : "fx:id=\"m_CheckMenuItem_ShowHeatCount\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_CheckMenuItem_ShowStatus ) : "fx:id=\"m_CheckMenuItem_ShowStatus\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_CheckMenuItem_ShowTime ) : "fx:id=\"m_CheckMenuItem_ShowTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_CheckMenuItem_Sound ) : "fx:id=\"m_CheckMenuItem_Sound\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_ChoiceBox_Program ) : "fx:id=\"m_ChoiceBox_Program\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Circle_Green ) : "fx:id=\"m_Circle_Green\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Circle_Red ) : "fx:id=\"m_Circle_Red\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_DisciplinesToggleGroup ) : "fx:id=\"m_DisciplinesToggleGroup\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Label_HeatCount ) : "fx:id=\"m_Label_HeatCount\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Label_SelectedDiscipline ) : "fx:id=\"m_Label_SelectedDiscipline\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Label_Status ) : "fx:id=\"m_Label_Status\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_LightBox ) : "fx:id=\"m_LightBox\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Slider_AfterTime ) : "fx:id=\"m_Slider_AfterTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_Slider_BeforeTime ) : "fx:id=\"m_Slider_BeforeTime\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( m_TextField_Time ) : "fx:id=\"m_TextField_Time\" was not injected: check your FXML file 'Main.fxml'.";
        assert nonNull( resources ) : "The resource bundle was not injected";

        //---* Load the sounds *-----------------------------------------------
        if( "arm".equalsIgnoreCase( getProperty( PROPERTY_CPUARCHITECTURE ) ) )
        {
            /*
             * Currently (as for 2017-08-19) JavaFX has issues to play media on
             * the Raspberry PI 3, due to a problem with libjfxmedia.so.
             * Therefore, we do now allow audio/sound on a Raspberry PI.
             */
            m_CheckMenuItem_Sound.setDisable( true );
            m_CheckMenuItem_Sound.setSelected( false );

            m_StartSound = Optional.empty();
            m_EndSound = Optional.empty();
        }
        else
        {
            //---* Sets the sound flag *---------------------------------------
            m_CheckMenuItem_Sound.setDisable( false );
            final var soundFlag = m_Preferences.getBoolean( PREF_NODE_Sound, false );
            m_CheckMenuItem_Sound.setSelected( soundFlag );

            //---* Load the audio clips *--------------------------------------
            var audioClipURL = getClass().getResource( SOUND_ENDSHOOTING );
            if( nonNull( audioClipURL ) )
            {
                var audioClip = new AudioClip( audioClipURL.toExternalForm() );
                audioClip.play();
                audioClip.stop();
                m_EndSound = Optional.of( audioClip );

                /*
                 * We try to load the start sound only when we have an end
                 * sound.
                 */
                audioClipURL = getClass().getResource( SOUND_STARTSHOOTING );
                if( nonNull( audioClipURL ) )
                {
                    audioClip = new AudioClip( audioClipURL.toExternalForm() );
                    audioClip.play();
                    audioClip.stop();
                    m_StartSound = Optional.of( audioClip );

                    //---* Sets the listener for the sound flag menu item *----
                    m_CheckMenuItem_Sound.selectedProperty().addListener( (p,o,n) -> m_Preferences.putBoolean( PREF_NODE_Sound, n ) );
                }
                else
                {
                    m_StartSound = Optional.empty();
                    m_CheckMenuItem_Sound.setDisable( true );
                    m_CheckMenuItem_Sound.setSelected( false );
                }
            }
            else
            {
                m_EndSound = Optional.empty();
                m_CheckMenuItem_Sound.setDisable( true );
                m_CheckMenuItem_Sound.setSelected( false );
            }
        }

        /*
         * Although this can be set also in the *.fxml file, it should be done
         * here.
         * This is because when it is set in *.fxml, the screen remain blank
         * until the mouse is moved over the menu or the button.
         */
        m_TextField_Time.setFocusTraversable( false );

        //---* Bind the "show time" flag to the time text field *--------------
        /*
         * For some reason, this could not be set in the *.fxgraph file.
         */
        m_TextField_Time.visibleProperty()
            .bind( m_CheckMenuItem_ShowTime.selectedProperty() );

        /*
         * The button pane should be visible only when at least one button is
         * enabled.
         */
        m_ButtonPane.visibleProperty()
            .bind( m_Button_Abort.disabledProperty().and( m_Button_Start.disabledProperty() ).not() );

        //---* Sets the "show time" flag *-------------------------------------
        final var showTimeFlag = m_Preferences.getBoolean( PREF_NODE_ShowTime, true );
        m_CheckMenuItem_ShowTime.setSelected( showTimeFlag );

        //---* Sets the listener for the "show time" flag menu item *----------
        m_CheckMenuItem_ShowTime.selectedProperty()
            .addListener( (p,o,n) -> m_Preferences.putBoolean( PREF_NODE_ShowTime, n ) );

        //---* Initialises the additional times *------------------------------
        final var prologTime = m_Preferences.getDouble( PREF_NODE_Before, 0.0 );
        m_Slider_BeforeTime.setValue( prologTime );
        m_Slider_BeforeTime.valueProperty()
            .addListener( (p,o,n) -> m_Preferences.putDouble( PREF_NODE_Before, n.doubleValue() ) );

        final var epilogTime = m_Preferences.getDouble( PREF_NODE_TimeAfter, 0.0 );
        m_Slider_AfterTime.setValue( epilogTime );
        m_Slider_AfterTime.valueProperty()
            .addListener( (p,o,n) -> m_Preferences.putDouble( PREF_NODE_TimeAfter, n.doubleValue() ) );

        //---* Set the change listener for the selected discipline *-----------
        m_SelectedDiscipline.addListener( this::onDisciplineChanged );

        //---* Get the currently selected discipline *-------------------------
        final var heatNumber = m_Preferences.getInt( PREF_NODE_Heat, 0 );
        final var disciplineName = m_Preferences.get( PREF_NODE_Discipline, DISCIPLINE_2_17.name() );
        final var discipline = Discipline.valueOf( disciplineName );
        m_DisciplinesToggleGroup.getToggles()
            .stream()
            .filter( toggle -> toggle.getUserData() == discipline )
            .findFirst()
            .ifPresent( m_DisciplinesToggleGroup::selectToggle );
        final var selectedToggle = m_DisciplinesToggleGroup.getSelectedToggle();
        if( nonNull( selectedToggle) )
        {
            m_SelectedDiscipline.set( (Discipline) selectedToggle.getUserData() );
        }

        //---* Set the listener for the heat changes *-------------------------
        m_ChoiceBox_Program.valueProperty()
            .addListener( (p,o,n) ->
            {
                final var program = m_SelectedDiscipline.getValue().getProgram();
                var h = -1;
                for( var i = 0; i < program.length; ++i )
                {
                    if( program [i] == n ) h = i;
                }
                if( h >= 0 ) m_Preferences.putInt( PREF_NODE_Heat, h );
            } );

        //---* Set the heat *--------------------------------------------------
        if( heatNumber != 0 )
        {
            final var heat = discipline.getProgram() [heatNumber];
            m_ChoiceBox_Program.setValue( heat );
        }

        //---* Configure the choice box for the program *----------------------
        m_ChoiceBox_Program.setConverter( Heat.getStringConverter() );

        //---* Set the change listener for the disciplines menu *--------------
        m_DisciplinesToggleGroup.selectedToggleProperty()
            .addListener( this::onDisciplineSelectionChanged );

        //---* Set the radius for the lights *---------------------------------
        m_Circle_Green.radiusProperty()
            .bind( Bindings.min( m_LightBox.widthProperty().divide( 2.0 ), m_LightBox.heightProperty() ).divide( 2.0 ).subtract( 20.0 ) );
        m_Circle_Red.radiusProperty()
            .bind( Bindings.min( m_LightBox.widthProperty().divide( 2.0 ), m_LightBox.heightProperty() ).divide( 2.0 ).subtract( 20.0 ) );

        //---* Create the light instances *------------------------------------
        m_GreenLight = new Light( Color.GREEN, m_Circle_Green::setFill );
        m_RedLight = new Light( Color.RED, m_Circle_Red::setFill );

        //---* Sets the show status flag *-------------------------------------
        final var showStatusFlag = m_Preferences.getBoolean( PREF_NODE_ShowStatus, false );
        m_CheckMenuItem_ShowStatus.setSelected( showStatusFlag );

        //---* Sets the listener for the show status flag menu item *----------
        m_CheckMenuItem_ShowStatus.selectedProperty()
            .addListener( (p,o,n) -> m_Preferences.putBoolean( PREF_NODE_ShowStatus, n ) );

        //---* Bind the status label to the status control *-------------------
        /*
        //m_Label_Status.textProperty().bind( m_StatusControl.asString() );
         *
         * Unfortunately this does not work as expected, as the label will not
         * be updated when the status changes.
         */

        m_Label_Status.visibleProperty()
            .bind( m_CheckMenuItem_ShowStatus.selectedProperty() );

        //---* Sets the show heat count flag *---------------------------------
        final var showHeatCountFlag = m_Preferences.getBoolean( PREF_NODE_ShowHeatCount, true );
        m_CheckMenuItem_ShowHeatCount.setSelected( showHeatCountFlag );

        //---* Sets the listener for the show heat count flag menu item *------
        m_CheckMenuItem_ShowHeatCount.selectedProperty()
            .addListener( (p,o,n) -> m_Preferences.putBoolean( PREF_NODE_ShowHeatCount, n ) );

        //---* Sets the size for the heat count label *------------------------
        if( "arm".equalsIgnoreCase( getProperty( PROPERTY_CPUARCHITECTURE ) ) )
        {
            m_Label_HeatCount.setStyle( "-fx-font-size : 30.0;" );
        }
        else
        {
            m_Label_HeatCount.setStyle( "-fx-font-size : 250.0;" );
        }

        //---* Set the initial state *-----------------------------------------
        reset();
    }   //  initialize()

    /**
     *  Sets the lights to the off state.
     */
    private final void lightsOff()
    {
        asList( m_RedLight, m_GreenLight )
            .forEach( Light::off );
    }   //  lightsOff()

    /**
     *  The button handler for the
     *  {@link #m_Button_Abort ABORT}
     *  button.
     *
     *  @param  ignoredEvent    The action event.
     */
    @FXML
    private final void onAbort( final ActionEvent ignoredEvent )
    {
        //---* Disable the abort button *--------------------------------------
        m_Button_Abort.setDisable( true );

        try( var ignored = m_HeatThreadGuard.lock() )
        {
            //---* Stop the heat thread *---------------------------------------
            if( nonNull( m_HeatThread ) && m_HeatThread.isAlive() )
            {
                var t = m_HeatThread;
                m_HeatThread = null;
                t.interrupt();
                try
                {
                    t.join( 1000 );
                }
                catch( @SuppressWarnings( "unused" ) final InterruptedException e ) { /* Deliberately ignored */ }
            }
        }

        //---* Reset the system *----------------------------------------------
        reset();
    }   //  onAbort()

    /**
     *  The change listener for the selected discipline.
     *
     *  @param  observable  The observed entity.
     *  @param  oldValue    The old value.
     *  @param  newValue    The new value.
     */
    private final void onDisciplineChanged( final ObservableValue<? extends Discipline> observable, final Discipline oldValue, final Discipline newValue )
    {
        if( nonNull( newValue ) )
        {
            final var text = newValue.toString();
            m_Label_SelectedDiscipline.setText( text );
            final var program = m_ChoiceBox_Program.getItems();
            program.clear();
            program.addAll( newValue.getProgram() );
            m_ChoiceBox_Program.setValue( program.get( 0 ) );

            //---* Update the preferences *------------------------------------
            m_Preferences.put( PREF_NODE_Discipline, newValue.name() );
            m_Preferences.putInt( PREF_NODE_Heat, 0 );
        }
    }   //  onDisciplineChanged()

    /**
     *  The change listener for the disciplines in the menu.
     *
     *  @param  observable  The observed entity.
     *  @param  oldValue    The old value.
     *  @param  newValue    The new value.
     */
    private final void onDisciplineSelectionChanged( final ObservableValue<? extends Toggle> observable, final Toggle oldValue, final Toggle newValue )
    {
        if( nonNull( newValue ) ) m_SelectedDiscipline.set( (Discipline) newValue.getUserData() );
    }   //  onDisciplineSelectionChanged()

    /**
     *  The handler method that initialises the termination of the application.
     *  It is assigned to the {@code MENU_FILE_EXIT} menu item.
     *
     *  @param  ignoredEvent    The action event.
     */
    @SuppressWarnings( "static-method" )
    @FXML
    private final void onExit( final ActionEvent ignoredEvent )
    {
        exit();
    }   //  onExit()

    /**
     *  Responds to the menu item {@code Help|About} and shows the
     *  &quot;About&quot; dialog.
     *
     *  @param  event   The event that triggered this handler.
     */
    @FXML
    private final void onMenuAbout( final ActionEvent event )
    {
        event.consume();
        try
        {
            final var mainClass = Main.class;

            /*
             * Get the parent window for the &quot;About&quot; window. This is
             * the owner of the menu.
             */
            final var ownerWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            //---* Get the user data from the scene of the parent window *-----
            final var mainSceneUserData = retrieveUserData( ownerWindow.getScene(), mainClass )
                .orElseThrow();
            final var application = mainSceneUserData.getApplication();

            //---* Create the new stage *--------------------------------------
            final var stage = new Stage( UTILITY );

            //---* Assign the parent *-----------------------------------------
            stage.initOwner( ownerWindow );

            //---* Load the *.fxml and create the scene root *-----------------
            final var fxmlFileName = "About.fxml";
            final var fxmlURL = mainClass.getResource( fxmlFileName );
            if( isNull( fxmlURL ) )
            {
                throw new ApplicationError( application.retrieveMessage( resources, MSGKEY_CannotLoadUIDefinition, fxmlFileName ) );
            }
            final Pane root = FXMLLoader.load( fxmlURL, resources );

            //---* Create the new scene *--------------------------------------
            SceneUserData.createScene( mainSceneUserData, stage, root );

            //---* Configure the stage *---------------------------------------
            stage.setAlwaysOnTop( true );
            stage.setTitle( resources.getString( "TITLE_ABOUT" ) );
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.showAndWait();
        }
        catch( final Exception e )
        {
            throw new ApplicationError( e );
        }
    }   //  onMenuAbout()

    /**
     *  The handler method for the
     *  {@link #m_Button_Start START}
     *  button.
     *
     *  @param  ignoredEvent   The action event.
     */
    @FXML
    private final void onStart( final ActionEvent ignoredEvent )
    {
        //---* Start the sequence *--------------------------------------------
        reset();
        proceedToNextStatus();

        try( var ignored = m_HeatThreadGuard.lock() )
        {
            //---* Execute the heat *------------------------------------------
            m_HeatThread = new Thread( this::executeHeat );
            m_HeatThread.setDaemon( true );
            m_HeatThread.start();
        }
    }   //  onStart()

    /**
     *  The handler method for touches to the grid pane.
     *
     *  @param  event   The touch event.
     */
    @FXML
    private final void onTouch( final TouchEvent event )
    {
        final var buttonEvent = new ActionEvent( event.getSource(), event.getTarget() );

        try( var ignored = m_HeatThreadGuard.lock() )
        {
            if( isNull( m_HeatThread ) || !m_HeatThread.isAlive() )
            {
                onStart( buttonEvent );
            }
            else
            {
                onAbort( buttonEvent );
            }
        }
    }   //  onTouch()

    /**
     *  The change listener for the preferences.
     *
     *  @param  event   The change event.
     */
    private final void preferenceChanged( final PreferenceChangeEvent event )
    {
        final var preferences = event.getNode();
        try
        {
            preferences.flush();
        }
        catch( final BackingStoreException e )
        {
            //---* We will not terminate the program … *-----------------------
            e.printStackTrace( err );
        }
    }   //  preferenceChanged()

    /**
     *  Proceeds to the next status.
     */
    @SuppressWarnings( "UnnecessaryDefault" )
    private final void proceedToNextStatus()
    {
        /*
         * We do not rely on the sequence the enum values of Status are defined
         * in; instead, we do it the verbose way.
         */
        try( var ignored = m_StatusControlGuard.lock() )
        {
            var currentStatus = m_StatusControl.get();
            var newStatus = switch( currentStatus )
            {
                case null, STATUS_OFF -> STATUS_STARTING;
                case STATUS_STARTING -> STATUS_BEFORE;
                case STATUS_BEFORE -> STATUS_PROLOG;
                case STATUS_PROLOG -> STATUS_SHOOTING;
                case STATUS_SHOOTING -> STATUS_AFTER;
                case STATUS_AFTER -> STATUS_OFF;
                default -> throw new UnsupportedEnumError( currentStatus );
            };

            //---* Apply the new status *--------------------------------------
            m_StatusControl.set( newStatus );
        }
    }   //  proceedToNextStatus()

    /**
     *  Resets the system status.
     */
    private final void reset()
    {
        try( var ignored = m_StatusControlGuard.lock() )
        {
            m_StatusControl.set( STATUS_OFF );
        }
    }   //  reset()

    /**
     *  Sets the system status to
     *  {@link Status#STATUS_PROLOG}.
     */
    private final void setStatusProlog()
    {
        try( var ignored = m_StatusControlGuard.lock() )
        {
            m_StatusControl.set( STATUS_PROLOG );
        }
    }   //  setStatusProlog()

    /**
     *  Sets the system status to
     *  {@link Status#STATUS_STARTING}.
     */
    private final void setStatusStarting()
    {
        try( var ignored = m_StatusControlGuard.lock() )
        {
            m_StatusControl.set( STATUS_STARTING );
        }
    }   //  setStatusStarting()

    /**
     *  The change listener for the status control.<br>
     *  <br>This method controls the current display.
     *
     *  @param  ignoredObservable   The observed entity; ignored.
     *  @param  oldStatus   The previous status.
     *  @param  newStatus   The new status.
     */
    private final void statusTransition( final ObservableValue<? extends Status> ignoredObservable, final Status oldStatus, final Status newStatus )
    {
        try( var ignored = m_StatusControlGuard.lock() )
        {
            if( nonNull( newStatus ) )
            {
                runLater( () -> m_Label_Status.setText( newStatus.toString() ) );

                StatusSwitch:
                switch( newStatus )
                {
                    case STATUS_AFTER -> {
                        //---* Stop the start sound and play the end sound *---
                        if( m_CheckMenuItem_Sound.isSelected() )
                        {
                            m_StartSound.ifPresent( s -> {
                                if( s.isPlaying() )
                                    s.stop();
                            } );
                            m_EndSound.ifPresent( AudioClip::play );
                        }

                        //---* Disable the abort button *----------------------
                        m_Button_Abort.setDisable( true );

                        //---* Change the lights *-----------------------------
                        m_RedLight.toggle();
                        m_GreenLight.toggle();

                        //---* Hide the heat count *---------------------------
                        m_Label_HeatCount.setVisible( false );
                    }
                    case STATUS_BEFORE -> {
                        //---* Disable the start button *----------------------
                        m_Button_Start.setDisable( true );

                        //---* Enable the abort button *----------------------
                        m_Button_Abort.setDisable( false );

                        //---* Focus the abort button *------------------------
                        runLater( m_Button_Abort::requestFocus );
                    }
                    case STATUS_OFF -> {
                        //---* Set the default time *--------------------------
                        m_TextField_Time.setText( format( "%3.1f", 0.0 ) );

                        //---* Enable the start button *-----------------------
                        m_Button_Start.setDisable( false );

                        //---* Disable the abort button *----------------------
                        m_Button_Abort.setDisable( true );

                        //---* Focus the start button *------------------------
                        runLater( m_Button_Start::requestFocus );

                        //---* Hide the heat count *---------------------------
                        m_Label_HeatCount.setVisible( false );

                        //---* Switch off the lights *-------------------------
                        lightsOff();
                    }
                    case STATUS_PROLOG -> {
                        if( oldStatus == STATUS_SHOOTING )
                        {
                            if( m_CheckMenuItem_Sound.isSelected() )
                            {
                                /*
                                 * Stop the start sound and play the end sound.
                                 */
                                m_StartSound.ifPresent( s -> {
                                    if( s.isPlaying() )
                                        s.stop();
                                } );
                                m_EndSound.ifPresent( AudioClip::play );
                            }

                            //---* Switch off the red light *------------------
                            m_GreenLight.off();
                        }

                        //---* Show the heat count *---------------------------
                        if( m_ChoiceBox_Program.getValue().isRepeated() && m_CheckMenuItem_ShowHeatCount.isSelected() )
                        {
                            m_Label_HeatCount.setVisible( true );
                        }

                        //---* Switch on the red light *-----------------------
                        m_RedLight.on();
                    }
                    case STATUS_SHOOTING -> {
                        //---* Play the start sound *--------------------------
                        if( m_CheckMenuItem_Sound.isSelected() )
                            m_StartSound.ifPresent( AudioClip::play );

                        //---* Switch on the green light *---------------------
                        m_GreenLight.on();

                        //---* Switch off the red light *----------------------
                        m_RedLight.off();

                        //---* Leave the switch *------------------------------
                        break StatusSwitch;
                    }

                    //---* Disable the start button *--------------------------
                    case STATUS_STARTING -> m_Button_Start.setDisable( true );

                    default -> throw new UnsupportedEnumError( newStatus );
                }   //  StatusSwitch:
            }
        }
    }   //  statusTransition()
}
//  class MainController

/*
 *  End of File
 */