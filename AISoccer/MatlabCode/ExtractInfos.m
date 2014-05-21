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
X1 = zeros(7,21*nb);
X2 = zeros(7,21*nb);
X1(7,:) = ones(1,21*nb);
X2(7,:) = ones(1,21*nb);
T1 = ones(1,21*nb);
T2 = -ones(1,21*nb);

for i=1:nb
    line = flines{i};
    values = sscanf(line, '%f');
    xt = values(1);
    yt = values(2);
    x1 = values(3);
    y1 = values(4);
    for j=1:21
        x2 = values(5+(j-1)*2);
        y2 = values(6+(j-1)*2);
        xp = [xt;yt;x1;y1;x2;y2];
        xn = [xt;yt;x2;y2;x1;y1];
        X1(:,j+(i-1)*21) = xp;
        X2(:,j+(i-1)*21) = xn;
    end
end

end

