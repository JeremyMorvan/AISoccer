function WriteInFile(FileName,W,V)

fid = fopen(FileName,'w');
format = [];
for i=1:size(W,2)
    format = [format '%f '];
end
format = [format '\n'];
fprintf(fid,format,W');
format = [];
for i=1:size(V,2)
    format = [format '%f '];
end
format = [format '\n'];
fprintf(fid,format,V');
fclose(fid);

end

