IF EXISTS ( SELECT * FROM sys.objects WHERE TYPE='P' AND NAME='spDeleteRequestsForCourierWithGivenUsername')
	DROP PROCEDURE spDeleteRequestsForCourierWithGivenUsername;
GO

CREATE PROCEDURE spDeleteRequestsForCourierWithGivenUsername
	@CourierUsername VARCHAR(100)
AS
BEGIN
	DELETE FROM Zahtev
	WHERE KorisnickoIme = @CourierUsername
END
GO
