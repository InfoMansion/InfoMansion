import React, {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';

const PI2 = Math.PI * 2;

const COLORS = [
  '#4b45ab',
  '#554fb8',
  '#605ac7',
  '#2a91a8',
  '#2e9ab2',
  '#32a5bf',
  '#81b144',
  '#85b944',
  '#8fc549',
  '#e0af27',
  '#eeba2a',
  '#fec72e',
  '#bf342d',
  '#ca3931',
  '#d7423a',
];

export class Polygon {
  constructor(x, y, radius, sides) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.sides = sides;
    this.rotate = 0;
  }

  animate(ctx, moveX) {
    ctx.save();
    const angle = PI2 / this.sides;
    const angle2 = PI2 / 6;

    ctx.translate(this.x, this.y);

    this.rotate += moveX * 0.0008;
    ctx.rotate(this.rotate);

    for (let i = 0; i < this.sides; i++) {
      const x = this.radius * Math.cos(angle * i);
      const y = this.radius * Math.sin(angle * i);

      i == 0 ? ctx.moveTo(x, y) : ctx.lineTo(x, y);

      ctx.save();
      const img = new Image();
      img.src =
        'notion://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F41b10e70-976e-4ae1-8a54-882169e20920%2FvectorLogo.svg?table=block&id=7cceb917-5a16-4d61-8dd5-6a966317b21a&spaceId=2ad0c350-f7c4-49fc-8e10-0df09d3d599e&userId=f7809fb4-8b68-4ac4-940d-e69a299a85e6&cache=v2';
      img.addEventListener('load', () => {
        ctx.drawImage(img, this.x, this.y);
      });
      ctx.fillStyle = COLORS[i];
      ctx.translate(x, y);
      ctx.rotate((((360 / this.sides) * i + 60) * Math.PI) / 180);
      ctx.beginPath();
      for (let j = 0; j < 6; j++) {
        const x2 = 300 * Math.cos(angle2 * j);
        const y2 = 300 * Math.sin(angle2 * j);
        j == 0 ? ctx.moveTo(x2, y2) : ctx.lineTo(x2, y2);
      }

      ctx.fill();
      ctx.closePath();
      ctx.restore();
    }
    ctx.restore();
  }
}

class Test {
  constructor() {
    this.canvas = document.createElement('canvas');
    this.canvas.style.position = 'absolute';
    this.canvas.style.top = '0';
    document.body.appendChild(this.canvas);
    this.ctx = this.canvas.getContext('2d');

    this.pixelRatio = window.devicePixelRatio > 1 ? 2 : 1;

    window.addEventListener('resize', this.resize.bind(this), false);
    this.resize();

    this.isDown = false;
    this.moveX = 0;
    this.offsetX = 0;

    document.addEventListener('pointerdown', this.onDown.bind(this), false);
    document.addEventListener('pointermove', this.onMove.bind(this), false);
    document.addEventListener('pointerup', this.onUp.bind(this), false);

    window.requestAnimationFrame(this.animate.bind(this));
  }

  resize() {
    this.stageWidth = document.body.clientWidth;
    this.stageHeight = document.body.clientHeight;

    this.canvas.width = this.stageWidth * this.pixelRatio;
    this.canvas.height = this.stageHeight * this.pixelRatio;
    this.ctx.scale(this.pixelRatio, this.pixelRatio);

    this.polygon = new Polygon(
      this.stageWidth / 2,
      this.stageHeight + this.stageHeight / 4,
      this.stageHeight / 1.5,
      15,
    );
  }

  animate() {
    window.requestAnimationFrame(this.animate.bind(this));

    this.ctx.clearRect(0, 0, this.stageWidth, this.stageHeight);

    this.moveX *= 0.92;

    this.polygon.animate(this.ctx, this.moveX);
  }

  onDown(e) {
    this.isDown = true;
    this.moveX = 0;
    this.offsetX = e.clientX;
  }

  onMove(e) {
    if (this.isDown) {
      this.moveX = e.clientX - this.offsetX;
      this.offsetX = e.clientX;
    }
  }

  onUp(e) {
    this.isDown = false;
  }
}

export default function App() {
  useEffect(() => {
    new Test();
  }, []);
  return null;
}
