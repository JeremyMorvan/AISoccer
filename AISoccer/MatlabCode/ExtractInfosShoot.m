function [X1,X2,T1,T2] = ExtractInfosShoot(FileNames)


X1 = [];
X2 = [];
T1 = [];
T2 = [];

for f=1:length(FileNames)
    FileName = FileNames{f};
    fid = fopen(FileName,'r');
    flines = {};
    while 1
        line = fgetl(fid);
        if ~ischar(line)
            break
        end
        flines = {flines{:} line};
    end
    fclose(fid);

    nb = length(flines);

    for i=1:nb
        line = flines{i};
        values = sscanf(line, '%f');
        if numel(values)==6
            i1 = values(1);
            if i1(1) ~= '%'     
                i2 = values(2);
                i3 = values(3);
                i4 = values(4);
                i5 = values(5);
                t = values(7);
                if t
                    T1 = [T1 t];
                    xp = [i1;i2;i3;i4;i5;i6;1];
                    X1 = [X1 xp];
                    
                    T1 = [T1 t];
                    xp = [-i1;i2;-i3;i4;i5;i6-sign(i3)*pi/2;1];
                    X1 = [X1 xp];
                else
                    T2 = [T2 -1];
                    xn = [i1;i2;i3;i4;i5;i6;1]; 
                    X2 = [X2 xn];
                    
                    T2 = [T2 -1];
                    xn = [-i1;i2;-i3;i4;i5;i6-sign(i3)*pi/2;1]; 
                    X2 = [X2 xn];
                end
                        
            end
        end
    end
end

end

