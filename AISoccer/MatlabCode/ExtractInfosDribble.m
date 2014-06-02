function [X1,X2,T1,T2] = ExtractInfosDribble(FileNames)


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
        if numel(values)>0
            
            i1 = values(1);
            if i1(1) ~= '%'
                nbJoueur = (numel(values)-1)/2;
                if nbJoueur>1
                    for j=1:nbJoueur
                        i2 = values(2+(j-1)*2);
                        i3 = values(3+(j-1)*2);
                        T1 = [T1 1];
                        xp = [i1;i2;i3;1]; 
                        X1 = [X1 xp];
                    end
                else
                    i2 = values(2);
                    i3 = values(3);
                    T2 = [T2 -1];
                    xn = [i1;i2;i3;1]; 
                    X2 = [X2 xn];
                end                   
            end
        end
    end
end

end

