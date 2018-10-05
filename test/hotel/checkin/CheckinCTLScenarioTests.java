package hotel.checkin;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import hotel.booking.BookingCTL;
import hotel.checkin.CheckinUI.State;
import hotel.checkout.CheckoutCTL;
import hotel.credit.CreditCardType;
import hotel.entities.Hotel;
import hotel.entities.RoomType;

@ExtendWith(MockitoExtension.class)
public class CheckinCTLScenarioTests
{
	@Spy Hotel hotel = new Hotel();
	@Mock CheckinUI checkinUI;
	BookingCTL bookingCtl = new BookingCTL(hotel);
	@Spy CheckoutCTL checkoutCtl = new CheckoutCTL(hotel);
	@InjectMocks CheckinCTL checkinCtl = new CheckinCTL(hotel);

	@BeforeEach
	void setup() throws Exception
	{
		hotel.addRoom(RoomType.SINGLE, 102);
	}

	@Test
	void CheckinToCheckedInRoomErrorMessage()
	{
		bookingCtl.phoneNumberEntered(1);
		bookingCtl.guestDetailsEntered("Test", "Address");
		bookingCtl.roomTypeAndOccupantsEntered(RoomType.SINGLE, 1);

		Date d = new GregorianCalendar(1111, 0, 1).getTime();
		bookingCtl.bookingTimesEntered(d, 1);
		bookingCtl.creditDetailsEntered(CreditCardType.MASTERCARD, 1, 1);

		checkinCtl.confirmationNumberEntered(101111102);
		checkinCtl.checkInConfirmed(true);

		checkinCtl.reset();
		checkinUI.setState(State.CHECKING);
		checkinUI.run();

		checkinCtl.confirmationNumberEntered(101111102);

		verify(checkinUI, never()).displayMessage("Room is not ready, sorry");
		verify(checkinUI).displayMessage("Booking 101111102 has already been checked in");
	}

}
