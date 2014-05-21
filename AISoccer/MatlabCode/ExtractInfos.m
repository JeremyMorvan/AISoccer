function [X1,X2,T1,T2] = ExtractInfos(FileName)

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
    close all;
    xt = values(1);
    yt = values(2);
    x1 = values(3);
    y1 = values(4);
%     plot(xt,yt,'ro');
%     hold on;
%     plot(x1,y1,'bx');
    for j=1:nbJoueur
        x2 = values(5+(j-1)*2);
        y2 = values(6+(j-1)*2);
%         plot(x2,y2,'gx');
        xp = [xt;yt;x1;y1;x2;y2;1];
        xn = [xt;yt;x2;y2;x1;y1;1];
        X1 = [X1 xp];
        X2 = [X2 xn];
        T1 = [T1 1];
        T2 = [T2 -1];
    end
%     pause();
end

end

