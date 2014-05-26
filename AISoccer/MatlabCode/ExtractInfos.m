function [X1,X2,T1,T2] = ExtractInfos(FileName)

ploter = 1;
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
X1 = [];
X2 = [];
T1 = [];
T2 = [];

for i=1:nb
    line = flines{i};
    values = sscanf(line, '%f');
    nbJoueur = (numel(values)/2)-2;
    if numel(values)>0
        if ploter
         close all;
        end
        pb = values(1);
        if pb>0.2
            if ploter
                plot(pb,0,'rx');
                hold on;
            end            
            x1 = values(2);
            y1 = values(3);
            if ploter
                plot(x1,y1,'bo');
            end
            for j=1:nbJoueur
                x2 = values(4+(j-1)*2);
                y2 = values(5+(j-1)*2);
                if ploter
                    plot(x2,y2,'go');
                end
                xp = [pb;x1;y1;x2;y2;1];  
                xn = [pb;x2;y2;x1;y1;1];
                X1 = [X1 xp];
                X2 = [X2 xn];
                T1 = [T1 1];
                T2 = [T2 -1];
            end
            if ploter
             pause();
            end
        end
    end
end

end

