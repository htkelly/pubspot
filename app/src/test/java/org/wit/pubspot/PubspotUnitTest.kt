package org.wit.pubspot

import android.net.Uri
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.wit.pubspot.models.PubspotMemStore
import org.wit.pubspot.models.PubspotModel
import org.wit.pubspot.models.PubspotStore

@RunWith(MockitoJUnitRunner::class)
class PubspotUnitTest {

    private lateinit var testPubs: PubspotStore

    private val aPub = PubspotModel(id = 0,
                            name = "Cleere's",
                            description = "A traditional pub",
                            rating = 5,
                            image = mock(Uri::class.java),
                            lat = 52.65521758561088,
                            lng = -7.2551263281857805,
                            zoom = 0f)

    private val anotherPub = PubspotModel(id = 0,
                            name = "Geoff's",
                            description = "A pub in Waterford",
                            rating = 5,
                            image = mock(Uri::class.java),
                            lat = 52.258191121727315,
                            lng = -7.111935643536935,
                            zoom = 0f)

    private val aThirdPub = PubspotModel(id = 0,
                            name = "Blue Bar",
                            description = "A blue pub",
                            rating = 5,
                            image = mock(Uri::class.java),
                            lat = 52.64891166318153,
                            lng = -7.251274457021695,
                            zoom = 0f)

    @Before
    fun setup(){
        testPubs = PubspotMemStore()
    }

    @After
    fun teardown() {
        testPubs.deleteAll()
    }

    @Test
    fun testFindAll() {
        testPubs.create(aPub)
        testPubs.create(anotherPub)
        testPubs.create(aThirdPub)
        assertEquals(3, testPubs.findAll().size)
    }

    @Test
    fun testCreate() {
        val beforeLength = testPubs.findAll().size
        testPubs.create(aPub)
        assertEquals(beforeLength + 1, testPubs.findAll().size)
    }

    @Test
    fun testUpdate () {
        val newDescription = "A pub in Kilkenny"
        val aModifiedPub = aPub.copy()
        aModifiedPub.description = newDescription
        testPubs.create(aPub)
        testPubs.update(aModifiedPub)
        assertEquals(newDescription, aPub.description)
    }

    @Test
    fun testDeleteOne() {
        testPubs.create(aPub)
        testPubs.create(anotherPub)
        testPubs.create(aThirdPub)
        val beforeLength = testPubs.findAll().size
        testPubs.delete(aPub)
        assertEquals(beforeLength - 1, testPubs.findAll().size)
    }

    @Test
    fun testDeleteAll() {
        testPubs.create(aPub)
        testPubs.create(anotherPub)
        testPubs.create(aThirdPub)
        assertEquals(3, testPubs.findAll().size)
        testPubs.deleteAll()
        assertEquals(0, testPubs.findAll().size)
    }
}