package cash.z.ecc.android.ui.home

import android.content.ComponentName
import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import cash.z.ecc.android.preference.Preferences
import cash.z.ecc.android.preference.SharedPreferenceFactory
import cash.z.ecc.android.preference.model.get
import cash.z.ecc.android.test.FragmentNavigationScenario
import cash.z.ecc.android.test.UiTestPrerequisites
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AutoshieldingInformationFragmentTest : UiTestPrerequisites() {
    @Test
    @MediumTest
    fun dismiss_returns_home() {
        val fragmentNavigationScenario = newScenario()

        onView(withId(cash.z.ecc.android.R.id.button_autoshield_dismiss)).also {
            it.perform(ViewActions.click())
        }

        assertThat(
            fragmentNavigationScenario.navigationController.currentDestination?.id,
            equalTo(cash.z.ecc.android.R.id.nav_home)
        )
    }

    @Test
    @MediumTest
    fun dismiss_sets_preference() {
        newScenario()

        onView(withId(cash.z.ecc.android.R.id.button_autoshield_dismiss)).also {
            it.perform(ViewActions.click())
        }

        assertThat(
            Preferences.isAcknowledgedAutoshieldingInformationPrompt.get(ApplicationProvider.getApplicationContext<Context>()),
            equalTo(true)
        )
    }

    @Test
    @MediumTest
    fun clicking_more_info_launches_browser() {
        val fragmentNavigationScenario = newScenario()

        onView(withId(cash.z.ecc.android.R.id.button_autoshield_more_info)).also {
            it.perform(ViewActions.click())
        }

        assertThat(
            fragmentNavigationScenario.navigationController.currentDestination?.id,
            equalTo(cash.z.ecc.android.R.id.nav_autoshielding_info_details)
        )

        // Note: it is difficult to verify that the browser is launched, because of how the
        // navigation component works.
    }

    @Test
    @MediumTest
    fun clicking_more_info_sets_preference() {
        newScenario()

        onView(withId(cash.z.ecc.android.R.id.button_autoshield_more_info)).also {
            it.perform(ViewActions.click())
        }

        assertThat(
            Preferences.isAcknowledgedAutoshieldingInformationPrompt.get(ApplicationProvider.getApplicationContext<Context>()),
            equalTo(true)
        )
    }

    @Test
    @MediumTest
    fun starting_fragment_does_not_launch_activities() {
        Intents.init()
        try {
            val fragmentNavigationScenario = newScenario()

            // The test framework launches an Activity to host the Fragment under test
            // Since the class name is not a public API, this could break in the future with newer
            // versions of the AndroidX Test libraries.
            intended(
                hasComponent(
                    ComponentName(
                        ApplicationProvider.getApplicationContext(),
                        "androidx.test.core.app.InstrumentationActivityInvoker\$BootstrapActivity"
                    )
                )
            )

            // Verifying that no other Activities (e.g. the link view) are launched without explicit
            // user interaction
            Intents.assertNoUnverifiedIntents()

            assertThat(
                fragmentNavigationScenario.navigationController.currentDestination?.id,
                equalTo(cash.z.ecc.android.R.id.nav_autoshielding_info)
            )
        } finally {
            Intents.release()
        }
    }

    @Test
    @MediumTest
    fun back_does_not_set_preference() {
        val fragmentNavigationScenario = newScenario()

        fragmentNavigationScenario.fragmentScenario.onFragment {
            // Probably closest we can come to simulating back with the navigation test framework
            fragmentNavigationScenario.navigationController.navigateUp()
        }

        assertThat(
            fragmentNavigationScenario.navigationController.currentDestination?.id,
            equalTo(cash.z.ecc.android.R.id.nav_home)
        )

        assertThat(
            Preferences.isAcknowledgedAutoshieldingInformationPrompt.get(ApplicationProvider.getApplicationContext<Context>()),
            equalTo(false)
        )
    }

    companion object {
        private fun newScenario(): FragmentNavigationScenario<AutoshieldingInformationFragment> {
            // Clear preferences for each scenario, as this most closely reflects how this fragment
            // is used in the app, as it is displayed usually on first launch
            SharedPreferenceFactory.getSharedPreferences(ApplicationProvider.getApplicationContext())
                .edit().clear().apply()

            val scenario = FragmentScenario.launchInContainer(
                AutoshieldingInformationFragment::class.java,
                null,
                cash.z.ecc.android.R.style.ZcashTheme,
                null
            )

            return FragmentNavigationScenario.new(
                scenario,
                cash.z.ecc.android.R.id.nav_autoshielding_info
            )
        }
    }
}
