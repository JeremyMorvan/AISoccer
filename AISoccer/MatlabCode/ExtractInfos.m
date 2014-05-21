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
X1 = zeros(7,20*nb);
X2 = zeros(7,20*nb);
X1(7,:) = ones(1,20*nb);
X2(7,:) = ones(1,20*nb);
T1 = ones(1,20*nb);
T2 = -ones(1,20*nb);

for i=1:nb
    line = flines{i};
    values = sscanf(line, '%f');
    x1 = values(1);
    y1 = values(2);
    x2 = values(3);
    y2 = values(4);
    xt = values(5);
    yt = values(6);
    xp = [x1;y1;x2;y2;xt;yt];
    xn = [x2;y2;x1;y1;xt;yt];
    X1(:,i) = xp;
    X2(:,i) = xn;
end

end

