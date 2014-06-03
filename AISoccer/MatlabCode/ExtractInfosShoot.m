function [X1,X2,T1,T2] = ExtractInfosShoot(FileNames)

ploter = 0;

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
                t = values(6);
                if ploter
                    close all;
                    figure;
                    if t
                        plot(i3,i4,'gx');
                    else
                        plot(i3,i4,'rx');
                    end
                    hold on;
                    axis equal;
                    plot(i1,i2,'x');
                    plot(i5,0,'o');
                    i1
                    i2
                    i3
                    i4
                    i5
                    pause;
                end
                i1 = i1-i5;
                i3 = i3-i5;
                if i1<0
                    i1=-i1;
                    i3=-i3;
                end
                x = [i1;i2;i3;i4;1];
                if t
                    T1 = [T1 1];                 
                    X1 = [X1 x];
                else
                    T2 = [T2 -1];                    
                    X2 = [X2 x];
                end                        
            end
        end
    end
end

end

