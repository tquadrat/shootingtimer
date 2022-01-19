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
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.shootingtimer.Main.BASE_BUNDLE_NAME;
import static org.tquadrat.shootingtimer.Main.MSG_CannotLoadTextResources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import javafx.util.StringConverter;

/**
 *  A <i>heat</i> is a part of a program for a shooting discipline. For the
 *  purpose of the Shooting Timer application, it defines the prolog time and
 *  the shooting time.
 *
 *  @version $Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public class Heat
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The implementation of
     *  {@link javafx.util.StringConverter javafx.util.StringConverter }
     *  for instances of this class.
     *
     *  @version $Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @UMLGraph.link
     *  @since 0.1.0
     */
    @ClassVersion( sourceVersion = "$Id: HexUtils.java 747 2020-12-01 12:40:38Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    private static class HeatConverter extends StringConverter<Heat>
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final String toString( final Heat object ) { return object.getName(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final Heat fromString( final String string ) { return null; }
    }
    //  class HeatConverter()

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the heat.
     */
    private final String m_Name;

    /**
     *  The prolog time in milliseconds.
     */
    private final long m_PrologTime;

    /**
     *  The number of times that the timings are to be repeated within a single
     *  heat.
     */
    private final int m_Repetitions;

    /**
     *  The shooting time in milliseconds.
     */
    private final long m_ShootingTime;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The one and only instance for the string converter.
     */
    private final static HeatConverter m_ConverterInstance = new HeatConverter();

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
     *  Creates a new {@code Heat} instance.
     *
     *  @param  name    The name of the heat, respectively the resource bundle
     *      key for this name.
     *  @param  shootingTime The shooting time in milliseconds.
     *  @param  prologTime  The prolog time in milliseconds.
     *  @param  repetitions The number of times that the timings are to be
     *      repeated within a single heat.
     */
    public Heat( final String name, final long shootingTime, final long prologTime, final int repetitions )
    {
        m_Name = retrieveText( m_ResourceBundle, requireNotEmptyArgument( name, "name" ) );
        m_PrologTime = prologTime;
        m_ShootingTime = shootingTime;
        m_Repetitions = repetitions;
    }   //  Heat()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the name for this heat.
     *
     *  @return The name.
     */
    public final String getName() { return m_Name; }

    /**
     *  Returns the prolog time for this heat-
     *
     *  @return The prolog time in milliseconds.
     */
    public final long getPrologTime() { return m_PrologTime; }

    /**
     *  The number of repetitions for these timings.
     *
     *  @return The repetitions.
     */
    public final int getRepetitions() { return m_Repetitions; }

    /**
     *  The shooting time for this heat.
     *
     *  @return The shooting time in milliseconds.
     */
    public final long getShootingTime() { return m_ShootingTime; }

    /**
     *  Returns the implementation of
     *  {@link StringConverter}
     *  for instances of this class.
     *
     *  @return The string converter.
     */
    public static final StringConverter<Heat> getStringConverter() { return m_ConverterInstance; }

    /**
     *  Returns whether this heat has repetitions.
     *
     *  @return {@code true} if the heat has repetitions, {@code false}
     *      otherwise.
     */
    public final boolean isRepeated() { return m_Repetitions > 1; }
}
//  class Heat

/*
 *  End of File
 */