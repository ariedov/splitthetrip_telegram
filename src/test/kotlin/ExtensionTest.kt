import com.dleibovych.splitthetrip.findFirstInt
import com.dleibovych.splitthetrip.findSecondInt
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExtensionTest {

    @Test
    fun testFirstInt() {
        val firstInt = "text 1".findFirstInt()

        assertEquals(1, firstInt)
    }

    @Test
    fun testNoFirstInt() {
        val noFirstInt = "asdf".findFirstInt()

        assertNull(noFirstInt)
    }

    @Test
    fun testFindSecondInt() {
        val secondInt = "1 aasdf 2".findSecondInt()

        assertEquals(2, secondInt)
    }

    @Test
    fun testFindNoSecondInt() {
        val secondInt = "1 aasdf".findSecondInt()

        assertNull(secondInt)
    }
}