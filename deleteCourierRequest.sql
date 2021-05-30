CREATE PROCEDURE spDeleteRequestsForCourierWithGivenUsername
	@CourierUsername VARCHAR(100)
AS
BEGIN
	DELETE FROM Zahtev
	WHERE KorisnickoIme = @CourierUsername
END
GO
