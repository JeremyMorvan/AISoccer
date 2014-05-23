function [xr,yr] = toRel(x,y,xb,yb,vxb,vyb)

xr1 = x-xb;
yr1 = y-yb;

ang = angle(vxb+1i*vyb);

xcomp = (xr1+1i*yr1).*(cos(ang)+1i*sin(ang));
xr = real(xcomp);
yr = abs(im(xcomp));

end

