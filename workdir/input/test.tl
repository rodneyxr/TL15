program
	var FLAG as bool ;
	var X as int ;
begin
	FLAG := true ;
	X := readint ;
	
	if FLAG then
		X := 1 ;
	else
		X := 0 ;
	end ;
	
	writeint X ;
end
