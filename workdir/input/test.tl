program 
	var X as int ;
	var FLAG as bool ;
begin

	FLAG := true ;
	X := readint ;

	if FLAG then
		writeint X ;
	else
		writeint X * 2 ;
	end ;

end 
