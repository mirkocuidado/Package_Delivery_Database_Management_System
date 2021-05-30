CREATE TRIGGER TR_TransportOffer_OfferAccepted 
   ON  Paket 
   AFTER UPDATE
AS 
BEGIN
	SET NOCOUNT ON

	DECLARE @CursorPaket CURSOR
	DECLARE @IdPaket INT

	SET @CursorPaket = CURSOR FOR
	SELECT IdPaket
	FROM inserted
	WHERE StatusIsporuke=1

	OPEN @CursorPaket

	FETCH NEXT FROM @CursorPaket
	INTO @IdPaket

	WHILE @@FETCH_STATUS = 0
	BEGIN
		-- Delete all the offers for the package
		DELETE FROM Ponuda
		WHERE IdPaket=@IdPaket

		FETCH NEXT FROM @CursorPaket
		INTO @IdPaket
	END

	CLOSE @CursorPaket
	DEALLOCATE @CursorPaket
END
GO
