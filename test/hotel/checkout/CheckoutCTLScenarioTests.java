package hotel.checkout;

import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.booking.BookingCTL;
import hotel.checkin.CheckinCTL;
import hotel.credit.CreditCardType;
import hotel.entities.Hotel;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
public class CheckoutCTLScenarioTests
{
	Hotel hotel = new Hotel();
	@Mock CheckoutUI checkoutUI;
	BookingCTL bookingCtl = new BookingCTL(hotel);
	CheckinCTL checkinCtl = new CheckinCTL(hotel);
	@InjectMocks CheckoutCTL checkoutCtl = new CheckoutCTL(hotel);

	@BeforeEach
	void setup() throws Exception
	{
		hotel.addRoom(RoomType.SINGLE, 102);
	}

	@Test
	void CheckoutACheckedOutRoom()
	{
		bookingCtl.phoneNumberEntered(1);
		bookingCtl.guestDetailsEntered("Test", "Address");
		bookingCtl.roomTypeAndOccupantsEntered(RoomType.SINGLE, 1);

		Date d = new GregorianCalendar(1111, 0, 1).getTime();
		bookingCtl.bookingTimesEntered(d, 1);
		bookingCtl.creditDetailsEntered(CreditCardType.MASTERCARD, 1, 1);

		checkinCtl.confirmationNumberEntered(101111102);
		checkinCtl.checkInConfirmed(true);

		checkoutCtl.roomIdEntered(102);
		checkoutCtl.chargesAccepted(true);
		checkoutCtl.creditDetailsEntered(CreditCardType.MASTERCARD, 1, 1);

		checkoutCtl.reset();

		checkoutCtl.roomIdEntered(102);

		verify(checkoutUI).displayMessage("No active booking found for room id 102");
	}

}
