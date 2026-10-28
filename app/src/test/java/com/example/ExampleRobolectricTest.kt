package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.coach.PsychologyTopics
import com.example.data.model.PreTradeState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("LMBO Gatekeeper", appName)
  }

  @Test
  fun `verify all 30 psychology topics exist`() {
    assertEquals(30, PsychologyTopics.allTopics.size)
    assertEquals(10, PsychologyTopics.getTopicsForPart(1).size)
    assertEquals(10, PsychologyTopics.getTopicsForPart(2).size)
    assertEquals(10, PsychologyTopics.getTopicsForPart(3).size)
  }

  @Test
  fun `verify break-even win rate calculation`() {
    val state = PreTradeState(payoutPct = 85.0)
    // 100 / (100 + 85) = 100 / 185 = ~54.054%
    assertEquals(54.054, state.breakEvenWinRate, 0.01)
  }

  @Test
  fun `verify pre-trade checklist gate validation`() {
    val state = PreTradeState()
    assertFalse(state.allConditionsPass)
  }
}
