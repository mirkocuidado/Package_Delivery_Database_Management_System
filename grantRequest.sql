CREATE PROCEDURE spAdminAcceptedUsersRequestForBecomingCourier 
	@KorisnickoImeKorisnikaKojiJePoslaoZahtev VARCHAR(100)
AS
BEGIN
	DECLARE @RegBr VARCHAR(100);

	SELECT @RegBr = (SELECT RegistarskiBroj FROM Zahtev WHERE KorisnickoIme=@KorisnickoImeKorisnikaKojiJePoslaoZahtev);

	IF ( NOT EXISTS ( SELECT * FROM Kurir WHERE RegistarskiBroj = @RegBr)) BEGIN
		INSERT INTO Kurir (KorisnickoIme, BrojPaketa, Profit, Status, RegistarskiBroj) VALUES (@KorisnickoImeKorisnikaKojiJePoslaoZahtev, 0,0,0,@RegBr);

		DELETE FROM Zahtev WHERE KorisnickoIme = @KorisnickoImeKorisnikaKojiJePoslaoZahtev;
	END
END
GO

