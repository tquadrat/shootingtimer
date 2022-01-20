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
import static org.tquadrat.foundation.i18n.I18nUtil.loadResourceBundle;
import static org.tquadrat.foundation.i18n.I18nUtil.retrieveText;
import static org.tquadrat.shootingtimer.Main.BASE_BUNDLE_NAME;
import static org.tquadrat.shootingtimer.Main.MSG_CannotLoadTextResources;

import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.i18n.Text;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  <p>{@summary The disciplines that the Shooting Timer application can be used
 *  for.}</p>
 *  <p>The name is based on the discipline number as used by the DSB.</p>
 *
 *  @version $Id: Discipline.java 106 2022-01-19 15:53:24Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: Discipline.java 106 2022-01-19 15:53:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public enum Discipline
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  The discipline &quot;Luftpistole Mehrkampf&quot; (Air Pistol Sport).
     */
    @Text
    (
        description = "Air Pistol DSB 2.17",
        translations =
        {
            @Translation( language="de", text = "· Luftpistole Mehrkampf (2.17)" ),
            @Translation( language="en", text = "·             Air Pistol DSB 2.17" )
        }
    )
    DISCIPLINE_2_17( "2.17", new Heat []
    {
        new Heat( "HEAT_PRECISION", 150_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 3_000, 7_000, 5 )
    } ),

    /**
     *  The discipline &quot;Luftpistole Standard&quot; (Air Pistol Standard).
     */
    @Text
    (
        description = "Air Pistol DSB 2.18",
        translations =
        {
            @Translation( language="de", text = "·  Luftpistole Standard (2.18)" ),
            @Translation( language="en", text = "·             Air Pistol DSB 2.18" )
        }
    )
    DISCIPLINE_2_18( "2.18", new Heat []
    {
        new Heat( "HEAT_150s", 150_000, 7_000, 1),
        new Heat( "HEAT_20s", 20_000, 7_000, 1 )
    } ),

    /**
     *  The discipline &quot;KK Sportpistole&quot; (Rimfire Sport Pistol).
     */
    @Text
    (
        description = "Rimfire Pistol Sport DSB 2.40",
        translations =
        {
            @Translation( language="de", text = "·       KK Sportpistole (2.40)" ),
            @Translation( language="en", text = "·   Rimfire Sport Pistol DSB 2.40" )
        }
    )
    DISCIPLINE_2_40( "2.40", new Heat []
    {
        new Heat( "HEAT_PRECISION", 300_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 3_000, 7_000, 5 )
    } ),

    /**
     *  The discipline &quot;Zentralfeuerpistole&quot; (Center Fire Pistol).
     */
    @Text
    (
        description = "Center Fire Pistol DSB 2.45",
        translations =
        {
            @Translation( language="de", text = "·  Zentralfeuerpistole  (2.45)" ),
            @Translation( language="en", text = "·     Center Fire Pistol DSB 2.45" )
        }
    )
    DISCIPLINE_2_45( "2.45", new Heat []
    {
        new Heat( "HEAT_PRECISION", 300_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 3_000, 7_000, 5 )
    } ),

    /**
     *  The discipline &quot;9mm Pistole&quot; (9mm Pistol).
     */
    @Text
    (
        description = "9mm Pistol DSB 2.53",
        translations =
        {
            @Translation( language="de", text = "·           9mm Pistole (2.53)" ),
            @Translation( language="en", text = "·             9mm Pistol DSB 2.53" )
        }
    )
    DISCIPLINE_2_53( "2.53", new Heat []
    {
        new Heat( "HEAT_PRECISION", 150_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 20_000, 7_000, 1 )
    } ),

    /**
     *  The discipline &quot;.357 Revolver&quot;.
     */
    @Text
    (
        description = ".357 Revolver DSB 2.55",
        translations =
        {
            @Translation( language="de", text = "·          .357 Revolver (2.55)" ),
            @Translation( language="en", text = "·           .357 Revolver DSB 2.55" )
        }
    )
    DISCIPLINE_2_55( "2.55", new Heat []
    {
        new Heat( "HEAT_PRECISION", 150_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 20_000, 7_000, 1 )
    } ),

    /**
     *  The discipline &quot;.44 Revolver&quot;.
     */
    @Text
    (
        description = ".44 Revolver DSB 2.58",
        translations =
        {
            @Translation( language="de", text = "·           .44 Revolver (2.58)" ),
            @Translation( language="en", text = "·            .44 Revolver DSB 2.58" )
        }
    )
    DISCIPLINE_2_58( "2.58", new Heat []
    {
        new Heat( "HEAT_PRECISION", 150_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 20_000, 7_000, 1 )
    } ),

    /**
     *  The discipline &quot;.44 Pistole&quot; (.44 Pistol).
     */
    @Text
    (
        description = ".45 Pistol DSB 2.59",
        translations =
        {
            @Translation( language="de", text = "·            .45 Pistole (2.59)" ),
            @Translation( language="en", text = "·              .45 Pistol DSB 2.59" )
        }
    )
    DISCIPLINE_2_59( "2.58", new Heat []
    {
        new Heat( "HEAT_PRECISION", 150_000, 7_000, 1),
        new Heat( "HEAT_DUEL", 20_000, 7_000, 1 )
    } ),

    /**
     *  The discipline &quot;KK Standardpistole&quot; (Rimfire Standard
     *  Pistol).
     */
    @Text
    (
        description = ".45 Pistol DSB 2.60",
        translations =
        {
            @Translation( language="de", text = "·     KK Standardpistole (2.60)" ),
            @Translation( language="en", text = "· Rimfire Standard Pistol DSB 2.60" )
        }
    )
    DISCIPLINE_2_60( "2.60", new Heat []
    {
        new Heat( "HEAT_150s", 150_000, 7_000, 1 ),
        new Heat( "HEAT_20s", 20_000, 7_000, 1 ),
        new Heat( "HEAT_10s", 10_000, 7_000, 1 )
    } ),

    /**
     *  Several tie-break.
     */
    @SuppressWarnings( "GrazieInspection" )
    @Text
    (
        description = "Tie-break",
        translations =
        {
            @Translation( language="de", text = "·                    Stechschuß" ),
            @Translation( language="en", text = "·                        Tie-break" )
        }
    )
    TIEBREAK( "Tiebreak 50s", new Heat []
    {
        new Heat( "TIEBREAK_30s", 30_000, 500, 1 ),
        new Heat( "TIEBREAK_50s", 50_000, 500, 1 ),
        new Heat( "TIEBREAK_75s", 75_000, 500, 1 )
    } );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The code number for this discipline as used by the DSB.
     */
    private final String m_DSBCode;

    /**
     *  The program for this discipline.
     */
    private final Heat [] m_Program;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The resource bundle with the names.
     */
    private final static ResourceBundle m_ResourceBundle;

    static
    {
        try
        {
            //---* Load the resource bundle with the texts *-------------------
            m_ResourceBundle = loadResourceBundle( BASE_BUNDLE_NAME )
                .orElseThrow( () -> new MissingResourceException( MSG_CannotLoadTextResources, BASE_BUNDLE_NAME, null ) );
        }
        catch( final MissingResourceException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Discipline} instance.
     *
     *  @param  dsbCode The code number for this discipline as used by the
     *      DSB (Deutscher Schützenbund).
     *  @param  program The program for this discipline.
     */
    private Discipline( final String dsbCode, final Heat [] program )
    {
        m_DSBCode = dsbCode;
        m_Program = program;
    }   //  Discipline()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Retrieves the discipline that matches the given DSB code number.
     *
     *  @param  dsbCode The code as used by the DSB.
     *  @return The discipline for the given code.
     *  @throws IllegalArgumentException    There is no discipline that matches
     *      the given code.
     */
    @SuppressWarnings( "unused" )
    public final static Discipline fromDSBCode( final String dsbCode ) throws IllegalArgumentException
    {
        final var retValue = Arrays.stream( values() )
                .filter( d -> d.getDSBCode().equals( dsbCode ) )
                .findAny()
                .orElseThrow( () -> new IllegalArgumentException( dsbCode ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromDSBCode()

    /**
     *  Returns the DSB code number.
     *
     *  @return The DSB code number.
     */
    public final String getDSBCode() { return m_DSBCode; }

    /**
     *  Returns the program.
     *
     *  @return The program.
     */
    public final Heat [] getProgram() { return m_Program; }

    /**
     *  {@inheritDoc}
     *
     *  @see Enum#toString()
     */
    @Override
    public final String toString()
    {
        final var retValue = retrieveText( m_ResourceBundle, this );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class Discipline

/*
 *  End of File
 */