(function () {
    var svg;
    var radius;
    var hexbin;
    var filter;
    var mainGroup;
    var addGroup;
    var hexAngles = d3.range(Math.PI / 6, 2 * Math.PI + Math.PI / 6, Math.PI / 3);
    var hexAngles2 = d3.range(Math.PI / 3, 2 * Math.PI + Math.PI / 3, Math.PI / 3);


    var prepareData = function(params, startX, startY, radius) {
        params[0].x = startX;
        params[0].y = startY;
        params[0].angle = hexAngles[0];
        var angles = hexAngles;
        var radiusPow = 2;

        for (var i = 1; i < params.length; i++) {
            if (i == 7) {
                angles = hexAngles2;
                radiusPow = 3.5;
                params[i].x = params[0].x;
                params[i].y = params[0].y - radius * 3.5;
                params[i].angle = angles[0];
                continue;
            }
            params[i].x = params[0].x + Math.sin(params[i - 1].angle) * radius * radiusPow;
            params[i].y = params[0].y - Math.cos(params[i - 1].angle) * radius * radiusPow;
            params[i].angle = angles[i % angles.length];
        }
    };

    var prepareDataForLinear = function(params, startX, startY, radius) {
        params[0].x = startX;
        params[0].y = startY;

        for (var i = 1; i < params.length; i++) {
            if (i % 3 === 0) {
                params[i].x = params[0].x;
                params[i].y = params[i-1].y + radius*2 + 20;
            } else {
                params[i].x = params[i-1].x + radius * 2;
                params[i].y = params[i-1].y;
            }
        }
    };


    function onAddParamClick(data) {
        var removeIndex = this.optParams.map(function(item) { return item.id; })
            .indexOf(data.id);
        this.defaultParams.push(this.optParams.splice(removeIndex, 1)[0]);

        addGroup
            .selectAll("*")
            .remove();
        mainGroup
            .selectAll("*")
            .remove();
        filter.attr('stdDeviation', 0);
        prepareData(this.defaultParams, svg[0][0].clientWidth / 2, svg[0][0].clientHeight / 2, radius);
        this.drawHex(this.defaultParams, mainGroup, onParamClick);
    }

    function onParamClick() {
        filter.attr('stdDeviation', 5);
        prepareDataForLinear(this.optParams, radius + 10, radius + 10, radius);
        this.drawHex(this.optParams, addGroup, onAddParamClick);
    }

    var extendControl = function(control) {
        var id = this.id;
        control.mode = 'disabled';
        control.turnOn = function() {
            if (this.mode === 'disabled') {
                this.active();
            }
        };
        control.active = function() {
            this.mode = 'active';
            $('#'+id+'-shadow').attr('visibility', 'visible');
            $('#' + id).attr('opacity', '1');
        };
        control.inactive = function() {
            this.mode = 'enabled';
            $('#'+id+'-shadow').attr('visibility', 'hidden');
        };
    };

    hexParams = function (config) {
        hexbin = d3.hexbin().radius(config.radius);
        svg = d3.select('#svg');
        filter = svg.append("defs")
            .append("filter")
            .attr("id", "blur")
            .append("feGaussianBlur")
            .attr("stdDeviation", 0);
        svg.append("defs")
            .append("filter")
            .attr("id", "shadow")
            .attr('x', '-2')
            .attr('y', '-2')
            .attr('width', '160')
            .attr('height', '160')
            .append("feGaussianBlur")
            .attr("stdDeviation", 2);

        mainGroup = svg.append('g')
            .attr('id', 'main')
            .attr('filter', 'url(#blur)');
        addGroup = svg.append('g')
            .attr('id', 'addParam');
        radius = config.radius;

        this.defaultParams = config.defaultParams;
        this.optParams = config.optParams;

        this.defaultParams.forEach(function(param) {
            if (param.boundedControl) {
                extendControl.call(param, param.boundedControl);
            }
        });
        this.optParams.forEach(function(param) {
            if (param.boundedControl) {
                extendControl.call(param, param.boundedControl);
            }
        });

        var self = this;


        this.drawHex = function(params, group, onClick) {
            var innerGroups = group
                .append('g')
                .selectAll('.hexagon')
                .data(params, function(d) {return d.id})
                .enter()
                .append('g')
                .attr('id', function(d) {return d.id;})
                .attr('opacity', '0.1');

            innerGroups.append("path")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + hexbin.hexagon();
                })
                .attr('id', function(d) {return d.id + '-shadow'})
                .attr('visibility', 'hidden')
                .attr('filter', 'url(#shadow)')
                .attr('fill', 'white')
                .attr("stroke-width", "15")
                .attr("stroke", function (d) {
                    return d.stroke
                });

            var paths = innerGroups.append("path")
                .attr("d", function (d) {
                    return "M" + d.x + "," + d.y + hexbin.hexagon();
                })
                .attr("stroke-width", "5px")
                .attr('stroke', 'white')
                .attr("class","hex-style")
                .transition()
                .attr("stroke", function (d) {
                    return d.stroke
                })
                .duration(1000);

            paths.each(function(path) {
                var pathDiv = d3.select(this);
                pathDiv.on('click', function(path) {
                    onClick.call(self, path);
                })
            });

            //Draw texts
            innerGroups
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y;
                })
                .attr("class","hex-text")
                .text(function (d) {
                    return d.text;
                })
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .transition()
                .duration(1000);
            innerGroups
                .append("text")
                .attr("x", function (d) {
                    return d.x;
                })
                .attr("y", function (d) {
                    return d.y + 20;
                })
                .attr('id', function (d) {
                    return d.id + '-text';
                })
                .text(function(d) {
                    if (d.value) {
                        return d.value();
                    }
                })
                .attr("class","hex-style")
                .attr("class","hex-text")
                .attr('onclick', function (d) {
                    return d.onclick;
                })
                .transition()
                .duration(1000);
        };
        prepareData(self.defaultParams, svg[0][0].clientWidth / 2, svg[0][0].clientHeight / 2, radius);
        this.drawHex(this.defaultParams, mainGroup, onParamClick);
    }
})();