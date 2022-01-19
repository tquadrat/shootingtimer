/*
 * ============================================================================
 * Copyright © 2002-2017 by Thomas Thrien.
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

import static org.tquadrat.foundation.i18n.I18nUtil.loadResourceBundle;
import static org.tquadrat.foundation.i18n.I18nUtil.retrieveText;
import static org.tquadrat.shootingtimer.Main.BASE_BUNDLE_NAME;
import static org.tquadrat.shootingtimer.Main.MSG_CannotLoadTextResources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.tquadrat.foundation.i18n.Text;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  The system status.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public enum Status
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  The system is in the OFF state, as for a reset.
     */
    @Text
    (
        description = "Status off",
        translations =
            {
                @Translation( language="de", text = "Inaktiv" ),
                @Translation( language="en", text = "Off" )
            }
    )
    STATUS_OFF,

    /**
     *  The system is starting a heat.
     */
    @Text
        (
            description = "Status Starting",
            translations =
                {
                    @Translation( language="de", text = "Start" ),
                    @Translation( language="en", text = "Start" )
                }
        )
    STATUS_STARTING,

    /**
     *  The system is in the status before the prolog: no light is on.
     */
    @Text
        (
            description = "Status before",
            translations =
                {
                    @Translation( language="de", text = "Vorlauf" ),
                    @Translation( language="en", text = "Before" )
                }
        )
    STATUS_BEFORE,

    /**
     *  The system is in the prolog status: the red light is on.
     */
    @Text
    (
        description = "Status Prolog",
        translations =
            {
                @Translation( language="de", text = "Bereit" ),
                @Translation( language="en", text = "Ready" )
            }
    )
    STATUS_PROLOG,

    /**
     *  The system is in the shooting status: the green light is on.
     */
    @Text
    (
        description = "Status Shooting",
        translations =
            {
                @Translation( language="de", text = "Schießen" ),
                @Translation( language="en", text = "Shoot" )
            }
    )
    STATUS_SHOOTING,

    /**
     *  The system is in the after status: again, no light is on.
     */
    @Text
    (
        description = "Status after",
        translations =
            {
                @Translation( language="de", text = "Nachlauf" ),
                @Translation( language="en", text = "Backlash" )
            }
    )
    STATUS_AFTER;

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

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
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
//  class Status

/*
 *  End of File
 */